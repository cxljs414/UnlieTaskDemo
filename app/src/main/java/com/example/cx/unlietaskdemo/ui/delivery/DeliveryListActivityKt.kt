package com.xstore.tms.android.ui.delivery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import com.jd.ace.utils.NetworkUtil
import com.leinyo.android.appbar.AppBar
import com.leinyo.easy_refresh.widget.ptr.PullToRefreshView
import com.xstore.tms.android.R
import com.xstore.tms.android.adapter.DeliveryListAdapterKt
import com.xstore.tms.android.base.BaseTitleActivityKt
import com.xstore.tms.android.contract.DeliveryListContract
import com.xstore.tms.android.core.Const
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.entity.delivery.DeliveryListEntity
import com.xstore.tms.android.entity.delivery.RequestOutbound
import com.xstore.tms.android.inerfaces.OnUnlineTaskStateChange
import com.xstore.tms.android.presenter.DeliveryListPresenter
import com.xstore.tms.android.receiver.UnlineTaskReceiver
import com.xstore.tms.android.utils.*
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_delivery_list.*

/**
 * Created by hly on 2018/3/16.
 * email hly910206@gmail.com
 */
class DeliveryListActivityKt : BaseTitleActivityKt<DeliveryListPresenter>(),
        DeliveryListContract.IView, DeliveryListAdapterKt.OnItemClickListener,
        PullToRefreshView.OnRefreshListener,OnUnlineTaskStateChange {

    companion object {
        const val SP_DO_LIST = "sp_do_list";
        const val EASY_OUTBOUND_REQUEST_CODE = 1001
        private const val SCAN_RESULT_PARAM = "scanResultParam"
        fun startDeliveryListActivity(context: Context, string: String) {
            val intent = Intent(context, DeliveryListActivityKt::class.java)
            intent.putExtra(SCAN_RESULT_PARAM, string)
            context.startActivity(intent)
        }
        fun startDeliveryListActivity(context: Context) {
            val intent = Intent(context, DeliveryListActivityKt::class.java)
            context.startActivity(intent)
        }
    }
    private var mList: MutableList<DeliveryListEntity> = ArrayList()
    private lateinit var mAdapter: DeliveryListAdapterKt
    private var mCollectionId: String = ""
    private var tempAddress: String? = null
    var needRemoveItemDoNoLocal:String = ""
    var neddRemoveItemPosition= -1
    private var unlineTaskReceiver= UnlineTaskReceiver(this)

    override fun getLayoutId(): Int {
        return R.layout.activity_delivery_list
    }

    override fun getTitleViewConfig(): AppBar.TitleConfig {
        return buildDefaultConfig(getString(R.string.title_delivery_list))
    }

    override fun initData() {
        super.initData()
        if (intent != null && intent.extras!= null) {
            mCollectionId = intent.extras.get(SCAN_RESULT_PARAM) as String
        }
        mAdapter = DeliveryListAdapterKt(this, mList)
        mAdapter.setClickListener(this)
        this.tv_outbound_key.setOnClickListener {
            when {
                mAdapter.selectedList.isEmpty() -> ToastUtils.showToast("请选择需要外呼的运单")
                mAdapter.selectedList.size > 20 -> ToastUtils.showToast("非常抱歉，一次外呼无法多于20单")
                else -> {
                    tempAddress = null
                    EasyOutboundActivity.startForResult(this, EASY_OUTBOUND_REQUEST_CODE, mAdapter.selectedList.size)
                }
            }
        }

        this.list_refresh.recyclerView.visibility = View.GONE
        this.list_refresh.setOnRefreshListener(this)
        this.list_refresh.setOnLoadMoreListener(null)

        loadData()
        this.nonet_retry.onClick {
            if(NetworkUtil.isConnected()){
                this.nonetwork.visibility = View.GONE
                loadData()
            }
        }

        var filter= IntentFilter()
        filter.addAction("xstore_unlinetask_success")
        filter.addAction("xstore_unlinetask_failure")
        registerReceiver(unlineTaskReceiver,filter)
    }

    private fun loadData(){
        if(NetworkUtil.isConnected()){
            this.list_refresh.setManualPullRefresh()
        }else{
            this.nonetwork.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EASY_OUTBOUND_REQUEST_CODE) {
            if(resultCode == EasyOutboundActivity.RESULT_CODE_CANCEL) {
                return
            }
            val address = data?.getCharSequenceExtra(EasyOutboundActivity.RESULT_KEY)
            if (address == null) {
                ToastUtils.showToast("未输入外呼地址")
                return
            }
            var sb = StringBuilder(100)
            mAdapter.selectedList.forEach { sb.append(it.doNo).append(',') }
            mAdapter.selectedList.clear()
            mAdapter.notifyDataSetChanged()
            // - 去掉最后一个逗号
            var doNos = sb.removeRange(sb.lastIndex, sb.lastIndex + 1)
            tempAddress = address.toString()
            mPresenter?.callOutbound(RequestOutbound(doNos.toString(), tempAddress!!))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getDeliveryList(list: List<DeliveryListEntity>?) {
        LogUtils.i(TAG,"列表刷新了一次")
        list?.forEach {
            TimeOutUtil.getInstance().addItem(it.doNo,it.leaveTime.toInt())
        }
        this.list_refresh.onLoadComplete(false)
        if(list == null || list.isEmpty()){
            this.tv_outbound_key.visibility = View.GONE
        }else{
            this.tv_outbound_key.visibility = View.VISIBLE
        }
        mAdapter.setData(list)
        this.list_refresh.recyclerView.visibility = View.VISIBLE
        this.list_refresh.setAdapter(mAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(unlineTaskReceiver)
    }

    override fun removeItem(position: Int) {
        mAdapter.removeItem(position)
    }

    override fun onClick(entity: DeliveryListEntity,position:Int) {
        if(entity.unlineTaskState == 1) {
            ToastUtils.showToast(getString(R.string.unlinetasking))
        }else{
            neddRemoveItemPosition = position
            needRemoveItemDoNoLocal = entity.doNo
            DeliveryDetailActivityKt.startDeliveryDetailActivity(this, entity.doNo)
        }
    }

    /**
     * 一键外呼返回
     */
    override fun outboundBack(result: Map<String, Int>) {
        if (tempAddress != null)
            EasyOutboundActivity.cacheAddress(tempAddress!!)
        mPresenter?.getDeliveryList()
    }

    override fun onLoadMoreRefresh(refreshView: PullToRefreshView?) {
        refreshView?.onLoadComplete(false)
    }

    override fun onPullRefresh(listView: PullToRefreshView?) {
        if(NetworkUtil.isConnected()){
            if(mAdapter.itemCount <= 1){
                this.list_refresh.recyclerView.visibility = View.GONE
            }else{
                this.list_refresh.recyclerView.visibility = View.VISIBLE
            }
            mPresenter?.getDeliveryList()
        }else{
            listView?.onLoadComplete(false)
            ToastUtils.showToast(R.string.no_network)
        }

    }

    override fun onLoadComplete() {
        this.list_refresh.onLoadComplete(false)
    }

    override fun showNoNet() {
        if(mAdapter.itemCount<=1){
            this.nonetwork.visibility = View.VISIBLE
        }else{
            ToastUtils.showToast(R.string.no_network)
        }
    }

    override fun onEventMain(event: EventDispatchManager.Event) {
        super.onEventMain(event)
        when(event.eventType){
            EventDispatchManager.EventType.EVENT_HINT_DELIVERYLIST->
                this.list_refresh.setManualPullRefresh()
            EventDispatchManager.EventType.EVENT_HINT_ALARM ->
                mPresenter?.updateLeaveTime()
            EventDispatchManager.EventType.EVENT_DELIVERY_STATUS_CHANGED->{
                val dono:String= event.data.toString()
                dono.let {
                    if(dono == needRemoveItemDoNoLocal){
                        mPresenter?.removeItem(neddRemoveItemPosition,true)
                    }else{
                        mPresenter?.removeItemByDoNo(dono,true)
                    }
                    TimeOutUtil.getInstance().remove(dono)
                }
            }
        }
    }

    override fun onUnlineTaskSuccess(dono: String, type: String?) {
        type.let {
            if(type == Const.UNLINETASK_TYPE_COMPLETE ||
                    type == Const.UNLINETASK_TYPE_REJECT){
                LogUtils.i(TAG,"unlinetask success  dono=$dono")
                if(!dono.startsWith("m")){
                    //如果某个任务执行成功，将列表对应的项删除
                    mPresenter?.removeItemByDoNo(dono,false)
                    //同时将已响列表删除
                    TimeOutUtil.getInstance().removeSounded(dono)
                }
            }
        }
    }

    override fun onUnlineTaskFailure(dono: String, type: String?) {
        type.let {
            if(type == Const.UNLINETASK_TYPE_COMPLETE ||
                    type == Const.UNLINETASK_TYPE_REJECT){
                LogUtils.i(TAG,"unlinetask failure  dono=$dono")
                if(!dono.startsWith("m")) {
                    mPresenter?.removeItemByDoNo(dono, true)
                }
            }
        }

    }
}