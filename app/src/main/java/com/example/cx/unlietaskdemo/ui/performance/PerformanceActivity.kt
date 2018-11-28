package com.xstore.tms.android.ui.performance

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BaseTitleActivityKt
import com.xstore.tms.android.base.IBasePresenter
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.utils.SysUtil
import kotlinx.android.synthetic.main.activity_performance.*
import org.apache.commons.lang3.StringUtils

/**
 * Created by wangwenming1 on 2018/4/3.
 */
class PerformanceActivity : BaseTitleActivityKt<IBasePresenter>() {

    companion object {
        /**
         * 导航数据定义
         */
        val today_delivery = Navigation("today", "截止本日","delivery", "妥投")
        val today_Reject = Navigation("today", "截止本日","reject", "拒收")
        val today_return = Navigation("today", "截止本日","return", "取件")
        val today_homeComplete = Navigation("today", "截止本日","homeComplete", "宅配妥投")
        val today_homeRejection = Navigation("today", "截止本日","homeRejection", "宅配拒绝")


        val lastMonth_delivery = Navigation("lastMonth", "截止上月","delivery", "妥投")
        val lastMonth_reject = Navigation("lastMonth", "截止上月","reject", "拒收")
        val lastMonth_return = Navigation("lastMonth", "截止上月","return", "取件")
        val lastMonth_homeComplete = Navigation("lastMonth", "截止上月","homeComplete", "宅配妥投")
        val lastMonth_homeRejection = Navigation("lastMonth", "截止上月","homeRejection", "宅配拒绝")

        val thisMonth_delivery = Navigation("thisMonth", "截止本月","delivery", "妥投")
        val thisMonth_reject = Navigation("thisMonth", "截止本月","reject", "拒收")
        val thisMonth_return = Navigation("thisMonth", "截止本月","return", "取件")

        val thisMonth_homeComplete = Navigation("thisMonth", "截止本月","homeComplete", "宅配妥投")
        val thisMonth_homeRejection = Navigation("thisMonth", "截止本月","homeRejection", "宅配拒绝")
        val naviList = listOf(
                listOf(today_delivery,today_Reject, today_return, today_homeComplete, today_homeRejection),
                listOf(lastMonth_delivery, lastMonth_reject,lastMonth_return, lastMonth_homeComplete, lastMonth_homeRejection),
                listOf(thisMonth_delivery,thisMonth_reject,  thisMonth_return,thisMonth_homeComplete, thisMonth_homeRejection))
    }

    override fun getLayoutId(): Int = R.layout.activity_performance

    override fun getTitleViewConfig() = buildDefaultConfig(R.string.title_complete_detail)

    override fun initData() {
        super.initData()
//        if (SysUtil.isDebug() && StringUtils.isBlank(LoginedCarrier.token)) {
//            LoginedCarrier.initLoginInfo(null,"xnsjzhangminjuan", "eed0d1d02cea40e6bb187ab8b2717752",null)
//        }
        this.vp.adapter = MyAdapter(supportFragmentManager)
        this.tabs.setupWithViewPager(vp)
    }

    private class MyAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val mFragments = arrayOfNulls<PerformanceTvFragment>(naviList.size)

        override fun getItem(position: Int): PerformanceTvFragment {
            if (mFragments[position] == null) {
                mFragments[position] = PerformanceTvFragment().apply { data = naviList[position] }
            }
            return mFragments[position]!!
        }

        override fun getCount(): Int {
            return naviList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return  getItem(position).data!![0].name
        }
    }


    data class Navigation(
            val key: String,
            val name: String,
            val subKey: String,
            val subName: String
    ) {
        var nav_1_2: String = key + "_" + subKey
            private set
    }
}