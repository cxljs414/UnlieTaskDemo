package com.xstore.tms.android.ui.homedelivery

import android.app.AlertDialog
import android.graphics.Color
import android.view.View
import com.leinyo.easy_refresh.widget.ptr.PullToRefreshView
import com.xstore.tms.android.R
import com.xstore.tms.android.adapter.WaitReceivingAdapter
import com.xstore.tms.android.base.BaseFragmentKt
import com.xstore.tms.android.contract.HomeDeliveryReceiveContract
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.entity.home.HomeDeliveryOrder
import com.xstore.tms.android.entity.home.ResponseHomeDeliveryDetail
import com.xstore.tms.android.presenter.HomeDeliveryPresenter
import com.xstore.tms.android.utils.DensityUtil
import com.xstore.tms.android.utils.TimeOutUtil
import com.xstore.tms.android.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_wait_receiving.*

/**
 * Created by hly on 2018/3/30.
 * email hly910206@gmail.com
 */
class WaitReceivingFragment : BaseFragmentKt<HomeDeliveryPresenter>(), HomeDeliveryReceiveContract.IView, PullToRefreshView.OnRefreshListener, View.OnClickListener {

    private lateinit var mAdapter: WaitReceivingAdapter
    private var mList: List<HomeDeliveryOrder> = ArrayList()


    override fun getLayoutId(): Int = R.layout.fragment_wait_receiving

    override fun initData() {
        super.initData()
        mAdapter = WaitReceivingAdapter(context!!, mList, this.pull_refresh)
        this.pull_refresh.addItemDecoration(SpaceItemDecoration(DensityUtil.dip2px(context, 10F)))
        this.pull_refresh.setAdapter(mAdapter)
        this.pull_refresh.setOnRefreshListener(this)
        this.tv_cancel.setOnClickListener(this)
        this.tv_confirm.setOnClickListener(this)
    }

    override fun getHomeDeliveryList(list: List<HomeDeliveryOrder>?, hasMore: Boolean) {
        if(mHasDestory)return
        if (list == null || list.isEmpty()) {
            /*if (this.activity is HomeDeliveryActivity) {
                (this.activity as HomeDeliveryActivity).selectWaitingReceive()
            }*/
            this.ll_bottom.visibility = View.GONE
        } else {
            this.ll_bottom.visibility = View.VISIBLE
        }
        mAdapter.setData(list)
        this.pull_refresh.onLoadComplete(hasMore)
        TimeOutUtil.getInstance().clearHomeCount()
    }

    override fun onLoadMoreRefresh(refreshView: PullToRefreshView?) {
        mPresenter?.getHomeReceiveList(false)
    }

    override fun onPullRefresh(listView: PullToRefreshView?) {
        mPresenter?.getHomeReceiveList(true)
    }

    override fun onResume() {
        super.onResume()
        this.pull_refresh.setManualPullRefresh()
    }

    private fun showAlert(isCancel: Boolean) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.alert_title)
        if (mList.any { it.isCheck }) {
            if (isCancel) {
                builder.setMessage(R.string.alert_delivery_cancel)

                builder.setPositiveButton(R.string.alert_confirm
                ) { _, _ -> mPresenter?.sendBack(true, mList) }
            } else {
                builder.setMessage(R.string.alert_delivery_confirm)

                builder.setPositiveButton(R.string.alert_confirm
                ) { _, _ -> mPresenter?.sendBack(false, mList) }
            }

            // 拒绝, 退出应用
            builder.setNegativeButton(R.string.cancel
            ) { _, _ -> }

        } else {
            builder.setMessage(R.string.alert_delivery_empty)
            builder.setPositiveButton(R.string.alert_confirm
            ) { _, _ -> }
        }
        builder.setCancelable(false)

        val dialog: AlertDialog = builder.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_cancel -> showAlert(true)
            R.id.tv_confirm -> showAlert(false)
        }
    }

    override fun sendBack(isSuccess: Boolean) {
        mPresenter?.getHomeReceiveList(true)
    }

    override fun getDeliveryDetail(entity: HomeDeliveryOrder) {

    }

    override fun onEventMain(event: EventDispatchManager.Event?) {
        super.onEventMain(event)
        if(event?.eventType == EventDispatchManager.EventType.EVENT_HINT_HOME_DELIVERY){
            this.pull_refresh.setManualPullRefresh()
        }
    }
}




