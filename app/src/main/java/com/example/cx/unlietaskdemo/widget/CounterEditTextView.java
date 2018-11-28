package com.example.cx.unlietaskdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xstore.tms.android.R;

/**
 * 统计输入字数的EditText
 * Created by yangxin on 15/9/12.
 */
public class CounterEditTextView extends RelativeLayout {

    private EditText mEtContent;
    private TextView mTvWordCounter;

    private int mMaxLength;
    private int mLines;
    private String mHint;
    private String mText;
    private float mTextSize;
    private int mPadding;
    private int mBackground;
    private TextWatcher mTextChangedListener;

    public void setTextChangedListener(TextWatcher textChangedListener) {
        mTextChangedListener = textChangedListener;
    }

    public CounterEditTextView(Context context) {
        this(context, null);
    }

    public CounterEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CounterEditTextView);
        mMaxLength = ta.getInt(R.styleable.CounterEditTextView_counter_max_length, 200);
        mLines = ta.getInt(R.styleable.CounterEditTextView_counter_lines, 100);
        mHint = ta.getString(R.styleable.CounterEditTextView_counter_hint);
        mText = ta.getString(R.styleable.CounterEditTextView_counter_text);
        mTextSize = ta.getDimension(R.styleable.CounterEditTextView_counter_text_size,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics()));
        mPadding = ta.getDimensionPixelOffset(R.styleable.CounterEditTextView_counter_padding, 10);
        mBackground = ta.getResourceId(R.styleable.CounterEditTextView_counter_background, R.drawable.bg_edittext_border);
        ta.recycle();
        LayoutInflater.from(context).inflate(R.layout.view_counter_edit_text, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTvWordCounter = (TextView) findViewById(R.id.tv_word_counter);
        mTvWordCounter.setText(getContext().getString(R.string.what_word_count, mMaxLength));
        mEtContent = (EditText) findViewById(R.id.et_content);
        mEtContent.setMaxLines(mLines);
        mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
        mEtContent.setHint(mHint);
        mEtContent.setText(mText);
        mEtContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mEtContent.setPadding(mPadding, mPadding, mPadding, mPadding);
        mEtContent.setBackgroundResource(mBackground);
        if (mLines < 2) {
            mTvWordCounter.setVisibility(View.GONE);
        } else {
            mEtContent.addTextChangedListener(new TextWatcherWrapper() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().startsWith("\n") && s.toString().endsWith("\n")) {

                    } else {
                        mTvWordCounter.setText(String.format("%d/%d", mMaxLength - s.toString().trim().length(), mMaxLength));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mTextChangedListener != null) {
                        mTextChangedListener.onTextChanged(s, 0, 0, 0);
                    }
                }
            });
        }
    }

    public String getText() {
        return mEtContent.getText().toString();
    }

    public void setText(String text) {
        mEtContent.setText(text);
    }
}
