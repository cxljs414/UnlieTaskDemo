package com.xstore.tms.android.ui.performance

import android.support.v7.app.AppCompatActivity
import com.leinyo.easy_refresh.widget.ptr.PullToRefreshView
import com.xstore.tms.android.R
import com.xstore.tms.android.adapter.PerformanceAdapter
import com.xstore.tms.android.base.BaseFragmentKt
import com.xstore.tms.android.contract.PerformanceContract
import com.xstore.tms.android.presenter.PerformancePresenter
import com.xstore.tms.android.utils.DensityUtil
import com.xstore.tms.android.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_performance_content.*
import java.util.*

/**
 * Created by wangwenming1 on 2018/4/3.
 */
class PerformanceContentFragment
    : BaseFragmentKt<PerformancePresenter>(), PerformanceContract.IView, PullToRefreshView.OnRefreshListener, PerformanceAdapter.OnClickListener {


    var dataSign: PerformanceActivity.Navigation? = null

    private lateinit var mAdapter: PerformanceAdapter

    private var mList = ArrayList<PerformanceContract.ItemData>()

    override fun getLayoutId(): Int = R.layout.fragment_performance_content

    override fun initData() {
        super.initData()
        mAdapter = PerformanceAdapter(context!!, mList, this.pull_refresh)
        mAdapter.mOnClick = this
        this.pull_refresh.addItemDecoration(SpaceItemDecoration(DensityUtil.dip2px(context, 1F)))
        this.pull_refresh.setAdapter(mAdapter)
        this.pull_refresh.setOnRefreshListener(this)
        this.pull_refresh.setManualPullRefresh()
        mPresenter?.loadData(dataSign!!)
    }

    override fun onDataReady(data: List<PerformanceContract.ItemData>, firstPage: Boolean) {
        if(mHasDestory)return
        if (data == null || data.isEmpty()) {
            this.pull_refresh.onLoadComplete(false)
        } else {
            mAdapter.setData(data, firstPage)
            this.pull_refresh.onLoadComplete(true)
        }
    }

    override fun onLoadMoreRefresh(refreshView: PullToRefreshView?) {
        mPresenter?.loadMore(dataSign!!)
    }

    override fun onPullRefresh(listView: PullToRefreshView?) {
        mPresenter?.refresh(dataSign!!)
    }

    override fun onClick(entity: PerformanceContract.ItemData) {
        val type = when (dataSign!!.subKey) {
            PerformanceActivity.lastMonth_delivery.subKey -> 1
            PerformanceActivity.lastMonth_reject.subKey -> 1
            PerformanceActivity.lastMonth_return.subKey -> 2
            PerformanceActivity.lastMonth_homeComplete.subKey, PerformanceActivity.lastMonth_homeRejection.subKey -> 3
            else -> -1
        }
        CourierPerformanceOrderDetailActivity.startActivity(this.activity as AppCompatActivity?, type, entity.number)
    }
}