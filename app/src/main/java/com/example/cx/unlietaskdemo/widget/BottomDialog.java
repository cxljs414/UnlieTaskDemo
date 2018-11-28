package com.example.cx.unlietaskdemo.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xstore.tms.android.R;



/**
 * Created by hly on 2017/11/16.
 * email hly910206@gmail.com
 */

public abstract class BottomDialog extends DialogFragment {

    public abstract View setView();

    private View mView;

    public final View getView() {
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.BottomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = setView();
        getDialog().getWindow().setWindowAnimations(R.style.AnimMenuPopup);
        setCancelable(true);
        initData();
        return mView;
    }

    protected void initData() {
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置弹出框宽屏显示，适应屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);

        //移动弹出菜单到底部
        WindowManager.LayoutParams wlp = getDialog().getWindow().getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        // wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(wlp);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
