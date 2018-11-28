package com.example.cx.unlietaskdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xstore.tms.android.R;

import java.math.BigDecimal;

/**
 * Created by hly on 2018/7/20.
 * email hly910206@gmail.com
 */
public class DeliveryErrorReasonView extends RelativeLayout {
    private String mName;
    private EditText mEditText;
    private LinearLayout btn_RadioButton;
    private ImageButton imb_CheckTip;
    private ImageView mSubImage, mPlusImage;
    private TextView mLeftText;
    private String mNumber;
    private float mTitleSize;
    private TextWatcher mTextWatch;
    private boolean isCountNum = false;
    private String unit;
    private boolean isChecked;
    private boolean isOverStep;
    private OverStepListener overStepListener;

    private OnClickListener checkListener;


    public DeliveryErrorReasonView(Context context) {
        this(context, null);

    }

    public DeliveryErrorReasonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        if (checked){
            imb_CheckTip.setImageResource(R.mipmap.radio);
            if(overStepListener != null){
                overStepListener.onChecked(true);
            }
        }else{
            imb_CheckTip.setImageResource(R.mipmap.radio_un);
        }
    }

    public boolean isCountNum() {
        return isCountNum;
    }

    public void setCountNum(boolean countNum) {
        isCountNum = countNum;
        if(!isCountNum){
            mEditText.setCursorVisible(false);
            mEditText.setFocusable(false);
            mEditText.setFocusableInTouchMode(false);
            mSubImage.setEnabled(false);
            mPlusImage.setEnabled(false);
        }
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public DeliveryErrorReasonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DeliveryErrorReasonView);
        mName = ta.getString(R.styleable.DeliveryErrorReasonView_title_name);

        ta.recycle();
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_delivery_error, this, true);
        btn_RadioButton = findViewById(R.id.btn_RadioButton);
        imb_CheckTip = findViewById(R.id.imb_CheckTip);
        mLeftText = findViewById(R.id.tv_name);
        mEditText = findViewById(R.id.et_number);
        mPlusImage = findViewById(R.id.iv_plus);
        mSubImage = findViewById(R.id.iv_sub);
        mLeftText.setText(mName);

        imb_CheckTip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkListener != null){
                    checkListener.onClick(v);
                }
            }
        });

        mPlusImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mNumber)){
                    return;
                }else {
                    BigDecimal tempNum = new BigDecimal(mNumber);
                    if (isCountNum){
                        setChecked(true);
                        if(!isOverStep){
                            BigDecimal tempResult = tempNum.add(new BigDecimal(1));
                            mNumber = String.valueOf(tempResult.intValue());
                            setmNumber(mNumber);
                        }
                    }
                }
            }
        });

        mSubImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mNumber)){
                    return;
                }else {
                    BigDecimal tempNum = new BigDecimal(mNumber);
                    if (tempNum.compareTo(BigDecimal.ZERO) == 1){
                        if (isCountNum){
                            BigDecimal tempResult = tempNum.subtract(new BigDecimal(1));
                            int resultValue = tempResult.intValue();
                            if (resultValue == 0){
                                setChecked(false);
                            }
                            mNumber = String.valueOf(resultValue);
                            isOverStep = false;
                            if(overStepListener!=null){
                                overStepListener.toggleInputStatu(isOverStep);
                            }
                            setmNumber(mNumber);
                        }
                    }
                }
            }
        });

        mEditText.addTextChangedListener(new TextWatcherWrapper() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mTextWatch != null){
                    mTextWatch.onTextChanged(s,start,before,count);
                }
            }
        });

    }


    public void setTitleText(String text){
        mLeftText.setText(text);
    }


    public void setmTextWatch(TextWatcher mTextWatch) {
        this.mTextWatch = mTextWatch;
    }

    public void setCheckListener(OnClickListener checkListener) {
        this.checkListener = checkListener;
    }

    public String getmNumber() {
        String number = mEditText.getText().toString();

        if (TextUtils.isEmpty(number)){
            return "0";
        }else{
            if (!isCountNum){
                number = number.replace(unit,"");
            }
            return  number;
        }
    }

    public void setmNumber(String  mNumber) {
        this.mNumber = mNumber;
        if (isCountNum){
            mEditText.setText(mNumber);
        }else{
            if (null == unit){
                unit = "";
            }
            mEditText.setText(mNumber+ unit);
        }

    }


    public boolean isOverStep() {
        return isOverStep;
    }

    public void setOverStep(boolean overStep) {
        isOverStep = overStep;
    }

    public OverStepListener getOverStepListener() {
        return overStepListener;
    }

    public void setOverStepListener(OverStepListener overStepListener) {
        this.overStepListener = overStepListener;
    }


    public ImageView getmSubImage() {
        return mSubImage;
    }

    public ImageView getmPlusImage() {
        return mPlusImage;
    }

    public interface OverStepListener{
        public void toggleInputStatu(boolean isOver);
        public void onChecked(boolean isChecked);
    }
}
