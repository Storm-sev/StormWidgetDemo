package com.example.customnewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import com.example.customnewdemo.databinding.LayoutCameraViewBinding

public class CustomCameraView : FrameLayout {


    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){



    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }


}