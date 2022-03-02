package com.example.customnewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.example.customnewdemo.utils.LogUtils

public  class CustomImageView : androidx.appcompat.widget.AppCompatImageView{

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    )


    private var width: Int? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = measuredWidth;

        LogUtils.d(TAG,"自定义view中获取的高度 --> $width")

        setMeasuredDimension(width!!,height!!)
    }

    private var height: Int? = null
    public fun setHeight(h:Int){
        this.height = h
    }

    companion object{
        val TAG = "CustomImageView"
    }
}