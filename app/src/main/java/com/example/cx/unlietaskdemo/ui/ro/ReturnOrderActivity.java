package com.example.cx.unlietaskdemo.ui.ro;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.leinyo.android.appbar.AppBar;
import com.xstore.tms.android.R;
import com.xstore.tms.android.adapter.ReturnOrderFragmentPagerAdapter;
import com.xstore.tms.android.base.BaseTitleActivityKt;
import com.xstore.tms.android.base.IBasePresenter;
import com.xstore.tms.android.core.event.EventDispatchManager;
import com.xstore.tms.android.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

/**
 * Created by wangwenming1 on 2018/3/20.
 */

public class ReturnOrderActivity extends BaseTitleActivityKt<IBasePresenter>
        implements ViewPager.OnPageChangeListener {

    private TabLayout tl_tab = null;

    private ViewPager vp_pager = null;

    private ReturnOrderFragmentPagerAdapter pagerAdapter = new ReturnOrderFragmentPagerAdapter(getSupportFragmentManager());

    @NotNull
    @Override
    public AppBar.TitleConfig getTitleViewConfig() {
        return buildDefaultConfig(R.string.title_return_order);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_return_order;
    }

    @Override
    protected void initData() {
        super.initData();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(1);
        }
        initViews();
        vp_pager.setAdapter(pagerAdapter);
        tl_tab.setupWithViewPager(vp_pager);
        vp_pager.addOnPageChangeListener(this);
    }

    @Override
    public void onEventMain(@NotNull EventDispatchManager.Event event) {
        super.onEventMain(event);
        if (event.eventType == 2) {
            pagerAdapter.loadUnreceivedData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //pagerAdapter.loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pagerAdapter.loadWaitingData();
    }


    /**
     * 显示带配送
     */
    public void selectWaitingReceive() {
        tl_tab.getTabAt(1).select();
        pagerAdapter.loadWaitingData();
    }

    public void selectUnreceived() {
        tl_tab.getTabAt(0).select();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        tl_tab = findViewById(R.id.tl_tab);
        vp_pager = findViewById(R.id.vp_pager);
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            //pagerAdapter.loadUnreceivedData();
        } else if (position == 1) {
            pagerAdapter.loadWaitingData();
        }
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
