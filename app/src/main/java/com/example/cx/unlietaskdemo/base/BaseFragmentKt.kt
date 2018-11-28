package com.xstore.tms.android.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xstore.tms.android.R
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.utils.ToastUtils
import java.lang.reflect.Constructor
import java.lang.reflect.ParameterizedType

/**
 * Created by hly on 2018/3/30.
 * email hly910206@gmail.com
 */
abstract class BaseFragmentKt<T : IBasePresenter> : Fragment(),EventDispatchManager.SubscriberListener {
    protected val TAG = this.javaClass.simpleName

    protected var mRootView: View? = null

    protected var mPresenter: T? = null

    protected var mHasInit: Boolean = false

    protected var mHasDestory:Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false)
        }
        return mRootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!mHasInit) {
            mPresenter = initPresenter()
            if (mPresenter != null) {
                mPresenter!!.setTag(TAG)
            }
            initData()
        }
        mHasInit = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHasDestory = true
        if (mRootView != null) {
            val parent = mRootView?.parent as ViewGroup
            parent.removeView(mRootView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter!!.destroy()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        if (activity != null) {
            activity!!.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
        }
    }

    private fun initPresenter(): T? {
        //获取泛型presenter
        if (javaClass.genericSuperclass is ParameterizedType) {

            val types: Class<T> = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>

            if (types.canonicalName == "com.xstore.tms.android.base.IBasePresenter") {
                return null
            }


            val viewTypes = javaClass.genericInterfaces
            var viewType: Class<*>? = null

            for (type in viewTypes) {
                if ((type as Class<*>).`package`.name == "com.xstore.tms.android.contract") {
                    viewType = type
                }
            }
            val parameterTypes: Array<Class<*>>
            val constructor: Constructor<T>

            return if (viewType != null) {
                parameterTypes = arrayOf(viewType)
                constructor = types.getConstructor(*parameterTypes)
                constructor.newInstance(this)
            } else {
                types.newInstance()
            }
        }
        return null
    }

    protected abstract fun getLayoutId(): Int

    /**
     * 初始化数据
     */
    open protected fun initData() {}

    override fun onEventMain(event: EventDispatchManager.Event?) {
    }

}