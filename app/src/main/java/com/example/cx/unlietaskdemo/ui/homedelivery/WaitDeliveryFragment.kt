package com.xstore.tms.android.ui.homedelivery

import com.leinyo.easy_refresh.widget.ptr.PullToRefreshView
import com.xstore.tms.android.R
import com.xstore.tms.android.adapter.WaitDeliveryAdapter
import com.xstore.tms.android.base.BaseFragmentKt
import com.xstore.tms.android.contract.HomeDeliveryReceiveContract
import com.xstore.tms.android.entity.home.HomeDeliveryOrder
import com.xstore.tms.android.entity.home.ResponseHomeDeliveryDetail
import com.xstore.tms.android.presenter.HomeDeliveryPresenter
import com.xstore.tms.android.utils.DensityUtil
import com.xstore.tms.android.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_wait_delivery.*
import org.apache.commons.lang3.StringUtils

/**
 * Created by hly on 2018/3/30.
 * email hly910206@gmail.com
 */
class WaitDeliveryFragment : BaseFragmentKt<HomeDeliveryPresenter>(), HomeDeliveryReceiveContract.IView, PullToRefreshView.OnRefreshListener {


    private lateinit var mAdapter: WaitDeliveryAdapter
    private var mList: List<HomeDeliveryOrder> = ArrayList()


    override fun getLayoutId(): Int = R.layout.fragment_wait_delivery

    /**
     * 刷新数据
     */
    fun manualPullRefresh() {
        this.pull_refresh.setManualPullRefresh()
    }

    override fun initData() {
        super.initData()
        mAdapter = WaitDeliveryAdapter(context!!, mList, this.pull_refresh)
        mAdapter.mOnClick = object : WaitDeliveryAdapter.OnClickListener {
            override fun onClick(entity: HomeDeliveryOrder) {
                if (StringUtils.isNoneBlank(entity.homeNo)) {
                    mPresenter?.getHomeDeliveryDetail(entity.homeNo!!)
                }
            }
        }
        this.pull_refresh.addItemDecoration(SpaceItemDecoration(DensityUtil.dip2px(context, 10F)))
        this.pull_refresh.setAdapter(mAdapter)
        this.pull_refresh.setOnRefreshListener(this)
    }

    override fun getHomeDeliveryList(list: List<HomeDeliveryOrder>?, hasMore: Boolean) {
        if(mHasDestory)return
        mAdapter.setData(list)
        this.pull_refresh.onLoadComplete(hasMore)
    }

    override fun onLoadMoreRefresh(refreshView: PullToRefreshView?) {
        mPresenter?.getHomeDeliveryList(false)
    }

    override fun onPullRefresh(listView: PullToRefreshView?) {
        mPresenter?.getHomeDeliveryList(true)
    }

    override fun onResume() {
        super.onResume()
        manualPullRefresh()
    }

    override fun sendBack(isSuccess: Boolean) {

    }

    override fun getDeliveryDetail(entity: HomeDeliveryOrder) {
        if (context != null) {
            HomeDeliveryDetailActivity.startHomeDeliveryDetailActivity(context!!, entity)
        }
    }
}