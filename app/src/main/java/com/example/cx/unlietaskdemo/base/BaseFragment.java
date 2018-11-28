package com.example.cx.unlietaskdemo.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xstore.tms.android.R;

/**
 * Created by hly on 16/7/27.
 * email hugh_hly@sina.cn
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG = this.getClass().getSimpleName();

    protected View mRootView;

    protected boolean mHasInit;

    protected IBasePresenter mPresenter;

//    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mHasInit) {
            mPresenter = getPresenter();
            findViews();
            initData();
        }
        mHasInit = true;
    }


    protected IBasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRootView != null) {
            ViewGroup parent = ((ViewGroup) mRootView.getParent());
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    protected View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        }
    }

    protected abstract int getLayoutId();

    /**
     * 查找控件
     */
    protected void findViews() {
        if (mPresenter != null) {
            mPresenter.setTag(TAG);
        }
//        unbinder = ButterKnife.bind(this, mRootView);
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }
}
