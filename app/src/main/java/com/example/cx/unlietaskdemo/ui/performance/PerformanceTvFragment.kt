package com.xstore.tms.android.ui.performance

import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BaseFragmentKt
import com.xstore.tms.android.contract.PerformanceTvContract
import com.xstore.tms.android.entity.net.performance.ResponseCompleteDoAndRoCount
import com.xstore.tms.android.presenter.PerformanceTvPresenter
import com.xstore.tms.android.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_performance_tv.*

/**
 * Created by wangwenming1 on 2018/4/3.
 */
class PerformanceTvFragment : BaseFragmentKt<PerformanceTvPresenter>(), PerformanceTvContract.IView  {

    var data: List<PerformanceActivity.Navigation>? = null

    override fun getLayoutId(): Int = R.layout.fragment_performance_tv

    override fun initData() {
        super.initData()

        this.vp.adapter = MyAdapter(childFragmentManager)
        this.tabs.setupWithViewPager(vp)

        mPresenter!!.loadCompleteCount(data!![0].key)
    }

    /**
     * 统计完成
     */
    override fun completeReady(data: ResponseCompleteDoAndRoCount) {
        if(mHasDestory)return
        this.tabs.tabMode = TabLayout.MODE_SCROLLABLE
        this.tabs.getTabAt(0)!!.text = "${this.data!![0].subName}\n(${data.completeDoNum})"
        this.tabs.getTabAt(1)!!.text = "${this.data!![1].subName}\n(${data.rejectDoNum})"
        this.tabs.getTabAt(2)!!.text = "${this.data!![2].subName}\n(${data.completeRoNum})"
        this.tabs.getTabAt(3)!!.text = "${this.data!![3].subName}\n(${data.completeHomeNum})"
        this.tabs.getTabAt(4)!!.text = "${this.data!![4].subName}\n(${data.rejectHomeNum})"
    }

    private inner class MyAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        var mFragments = arrayOfNulls<PerformanceContentFragment>(data!!.size)

        override fun getItem(position: Int): PerformanceContentFragment {
            if (mFragments[position] == null) {
                mFragments[position] = PerformanceContentFragment().apply { dataSign = data!![position] }
            }
            return mFragments[position]!!
        }

        override fun getCount(): Int {
            return data!!.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return getItem(position).dataSign!!.subName
        }
    }

}