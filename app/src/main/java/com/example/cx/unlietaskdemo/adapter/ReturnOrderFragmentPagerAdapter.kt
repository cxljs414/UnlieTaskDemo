package com.xstore.tms.android.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.xstore.tms.android.ui.ro.ReturnOrderFragment

/**
 * Created by wangwenming1 on 2018/3/20.
 */

class ReturnOrderFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val COUNT = 2
    private val titles = arrayOf("未接单", "待取件")
    private val contents = arrayOf(
            ReturnOrderFragment.newInstance(ReturnOrderFragment.RETURN_ORDER_STATUS_UNRECEIVED),
            ReturnOrderFragment.newInstance(ReturnOrderFragment.RETURN_ORDER_STATUS_WAITING_RECEIVE)
    )

    fun loadWaitingData(){
        contents[1].loadData()
    }

    fun loadUnreceivedData() {
        contents[0].loadData()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getItem(position: Int): Fragment {
        return contents[position]
    }

    override fun getCount(): Int {
        return COUNT
    }
}
