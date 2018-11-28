package com.xstore.tms.android.ui.homedelivery

import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.leinyo.android.appbar.AppBar
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BaseTitleActivityKt
import com.xstore.tms.android.base.IBasePresenter
import kotlinx.android.synthetic.main.activity_home_delivery.*
import kotlinx.android.synthetic.main.activity_performance.view.*
import java.util.*

/**
 * Created by hly on 2018/3/30.
 * email hly910206@gmail.com
 */
class HomeDeliveryActivity : BaseTitleActivityKt<IBasePresenter>(), ViewPager.OnPageChangeListener {
    private val mFragments = ArrayList<android.support.v4.app.Fragment>()
    private val mMyAdapter: MyAdapter = MyAdapter(supportFragmentManager)
    private var mStrings = arrayOfNulls<String>(2)
    private var waitDeliveryFragment: WaitDeliveryFragment? = null

    override fun getLayoutId(): Int = R.layout.activity_home_delivery

    override fun getTitleViewConfig(): AppBar.TitleConfig = buildDefaultConfig(R.string.title_home_delivery)

    override fun initData() {
        super.initData()
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
        mStrings = resources.getStringArray(R.array.home_delivery)
        waitDeliveryFragment = WaitDeliveryFragment()
        mFragments.add(WaitReceivingFragment())
        mFragments.add(waitDeliveryFragment!!)
        this.vp.adapter = mMyAdapter
        this.tabs.setupWithViewPager(this.vp)
        this.vp.addOnPageChangeListener(this)
    }

    /**
     * 显示待配送
     */
    fun selectWaitingReceive() {
        mFragments[1].onResume()
        this.tabs.getTabAt(1)?.select()
    }

    internal inner class MyAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mStrings[position]
        }
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager.SCROLL_STATE_IDLE
     *
     * @see ViewPager.SCROLL_STATE_DRAGGING
     *
     * @see ViewPager.SCROLL_STATE_SETTLING
     */
    override fun onPageScrollStateChanged(state: Int) {
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position Position index of the first page currently being displayed.
     * Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    override fun onPageSelected(position: Int) {
        if (position == 1)
            waitDeliveryFragment!!.manualPullRefresh()

    }
}