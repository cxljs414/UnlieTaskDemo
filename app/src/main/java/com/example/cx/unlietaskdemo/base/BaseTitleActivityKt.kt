package com.xstore.tms.android.base

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.leinyo.android.appbar.AppBar
import com.xstore.tms.android.R
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.include_title_layout.*

/**
 * Created by hly on 2018/3/16.
 * email hly910206@gmail.com
 */
abstract class BaseTitleActivityKt<IBasePresenter : com.xstore.tms.android.base.IBasePresenter> : BaseActivityKt<IBasePresenter>() {

    protected lateinit var mToolbar: AppBar
    private lateinit var mContentLayout: FrameLayout

    override fun initWindows() {
        super.initWindows()
        delegate.setContentView(R.layout.activity_base)
        mContentLayout = this.content
        mToolbar = this.toolbar
        mToolbar.setTitleConfig(getTitleViewConfig())

        setSupportActionBar(mToolbar)
        //不显示Toolbar title
        if (supportActionBar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun setContentView(layoutResID: Int) {
        if (layoutResID != 0) {
            mContentLayout.removeAllViews()
            layoutInflater.inflate(layoutResID, mContentLayout, true)
        }
    }

    override fun setContentView(view: View) {
        mContentLayout.removeAllViews()
        mContentLayout.addView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        mContentLayout.addView(view, params)
    }

    abstract fun getTitleViewConfig(): AppBar.TitleConfig

    open fun buildDefaultConfig(title: String): AppBar.TitleConfig {
        val config = AppBar.TitleConfig(title)
        config.setLeftOnClickListener(mBackOnClickListener)
        return config
    }

    open fun buildDefaultConfig(titleId: Int): AppBar.TitleConfig {
        return buildDefaultConfig(getString(titleId))
    }

    /**
     * 默认左侧按钮点击事件
     */
    protected var mBackOnClickListener: View.OnClickListener = View.OnClickListener { finish() }
}