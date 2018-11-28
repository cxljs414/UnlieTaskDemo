package com.example.cx.unlietaskdemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected IBasePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindows();

        mPresenter = getPresenter();

        if (mPresenter != null) {
            mPresenter.setTag(TAG);
        }
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        initData();
    }

    protected abstract int getLayoutId();

    protected IBasePresenter getPresenter() {
        return null;
    }

    protected void initWindows() {

    }

    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
    }
}
