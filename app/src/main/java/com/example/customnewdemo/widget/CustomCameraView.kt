package com.example.customnewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import com.example.customnewdemo.databinding.LayoutCameraViewBinding
import com.example.customnewdemo.utils.LogUtils

public class CustomCameraView : FrameLayout {

    companion object{
        val TAG = "CustomCameraView"
    }
    constructor(context: Context) : super(context){
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        LogUtils.d(TAG,"方法参数  2")
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        LogUtils.d(TAG,"方法参数  3")

    }


//    constructor(context: Context) : this(context,null)
//    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
//        context,
//        attrs,
//        defStyleAttr
//    ){
//
//
//
//    }



    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }


}