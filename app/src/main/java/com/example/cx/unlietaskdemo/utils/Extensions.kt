package com.xstore.tms.android.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.xstore.tms.android.R
import com.xstore.tms.android.widget.CounterEditTextView
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.reflect.Field

fun View.onClick(callback:(View)->Unit){
    this.setOnClickListener {
        callback(it)
    }
}

fun View.showNoNet(listener:((view:View) -> Unit)){
    val snackbar = Snackbar.make(this,"网络较差，请点击重试",Snackbar.LENGTH_LONG)
            .setAction("重试",listener)
            .setActionTextColor(Color.parseColor("#FF3827"))
    snackbar.view.setBackgroundColor(Color.parseColor("#204560"))
    snackbar.show()
}

fun EditText.textWatch(callback: (str:String) -> Unit){
    this.addTextChangedListener(object:TextWatcher{
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback(s.toString())
        }

    })
}

fun CounterEditTextView.textChange(callback: (str:String) -> Unit){
    this.setTextChangedListener(object:TextWatcher{
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback(s.toString())
        }

    })
}

fun EditText.disableCopyAndPaste(){
    this.setOnLongClickListener { return@setOnLongClickListener true }
    this.isLongClickable = false
    this.setOnTouchListener { v, event ->
        if (event?.action == MotionEvent.ACTION_DOWN) {
            // setInsertionDisabled when user touches the view
            setInsertionDisabled(this@disableCopyAndPaste)
        }

        false
    }
}

fun setInsertionDisabled(editText:EditText ) {
    try {
        var editorField: Field = TextView::class.java.getDeclaredField("mEditor");
        editorField.isAccessible = true
        var editorObject = editorField.get(editText)
        var editorClass = Class.forName("android.widget.Editor");
        var mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled")
        mInsertionControllerEnabledField.isAccessible = true
        mInsertionControllerEnabledField.set(editorObject, false)
        var mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled")
        mSelectionControllerEnabledField.isAccessible = true
        mSelectionControllerEnabledField.set(editorObject, false)
    } catch (e:Exception ) {
        e.printStackTrace()
    }
}


//复制文本到剪贴板
fun Context.copy(content:String){
    var clipboard: ClipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    var clipData = ClipData.newPlainText(null,content)
    clipboard.primaryClip = clipData
}

fun EditText.showPassword(isShow:Boolean){
    val pos = this.selectionStart
    if(isShow){
        this.transformationMethod = HideReturnsTransformationMethod.getInstance()
    }else{
        this.transformationMethod = PasswordTransformationMethod.getInstance()

    }
    this.setSelection(pos)
}

fun CheckBox.onCheckChanged(callback:(isChecked:Boolean)->Unit){
    this.setOnCheckedChangeListener{_,isChecked->callback(isChecked)}
}

fun View.startAnimation(context:Context,resId:Int){
    val animation = AnimationUtils.loadAnimation(context, resId)
    this.startAnimation(animation)
}