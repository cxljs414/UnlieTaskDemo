package com.example.cx.unlietaskdemo.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xstore.tms.android.R;
import com.xstore.tms.android.utils.imageloader.GlideLoaderUtil;


/**
 * Created by hly on 16/5/6.
 * email hugh_hly@sina.cn
 */
public class ImageDialog extends DialogFragment {

    ImageView mTvMsg;

    private String mMessage;
    private static final String KEY_MESSAGE = "key_message";

    public static ImageDialog createDialogFragment(String message) {
        ImageDialog loadingDialogFragment = new ImageDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, message);
        loadingDialogFragment.setArguments(bundle);
        return loadingDialogFragment;
    }

    public ImageDialog show(AppCompatActivity context) {
        FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
        ft.add(this, "image");
        ft.commitAllowingStateLoss();
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMessage = getArguments().getString(KEY_MESSAGE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ImageDialogStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_image, null);
        mTvMsg = view.findViewById(R.id.tv_msg);
        GlideLoaderUtil.loadNormalImage(getContext(), mMessage, R.drawable.ic_default_square_small, mTvMsg);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        setCancelable(true);
        //int width = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.5);
        //int height = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.3);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setGravity(Gravity.CENTER);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
