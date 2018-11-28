package com.example.cx.unlietaskdemo.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xstore.tms.android.R;

import java.lang.ref.WeakReference;


/**
 * Created by hly on 16/5/6.
 * email hugh_hly@sina.cn
 */
public class LoadingDialog extends DialogFragment {

    //    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    //    @BindView(R.id.tv_msg)
    TextView mTvMsg;

    //    Unbinder unbinder;
    private String mMessage;
    private static final String KEY_MESSAGE = "key_message";

    private MyHandler mMyHandler = new MyHandler(Looper.getMainLooper(), this);

    public static LoadingDialog createDialogFragment(String message) {
        LoadingDialog loadingDialogFragment = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, message);
        loadingDialogFragment.setArguments(bundle);
        return loadingDialogFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessage = getArguments().getString(KEY_MESSAGE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.LoadingDialogStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_loading, null);
        mLlContent = view.findViewById(R.id.ll_content);
        mTvMsg = view.findViewById(R.id.tv_msg);
//        unbinder = ButterKnife.bind(this, view);
        mTvMsg.setText(mMessage);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        //int width = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.5);
        //int height = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.3);
        mLlContent.setGravity(Gravity.CENTER);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setGravity(Gravity.CENTER);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setMessage(String message) {
        Message message1 = mMyHandler.obtainMessage();
        message1.obj = message;
        mMyHandler.sendMessage(message1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    class MyHandler extends Handler {
        private final WeakReference<LoadingDialog> mActivity;

        public MyHandler(Looper looper, LoadingDialog activity) {
            super(looper);
            mActivity = new WeakReference<LoadingDialog>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoadingDialog activity = mActivity.get();
            if (activity != null) {
                mTvMsg.setText(String.valueOf(msg.obj));
            }
        }
    }
}
