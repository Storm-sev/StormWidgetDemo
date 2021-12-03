package com.storm.navdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.customnewdemo.app.MyApplication
import com.example.customnewdemo.utils.dip2px


class DotView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val paint : Paint by lazy {

        val paint = Paint()
        with(paint){
            isAntiAlias = true
            paint.setColor(Color.RED)

        }
        paint
    }

    public var show : Boolean = false
        set(value) {
        field = value
        invalidate()
    }
    get() = field

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (show) {
            canvas?.drawCircle(mWidth.toFloat()/2,mWidth.toFloat()/2,mWidth.toFloat()/2,paint)

        }
    }


    private var mWidth: Int = 0
    private var mHeight : Int = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = getMeasureSize(widthMeasureSpec, true)
        mHeight = getMeasureSize(heightMeasureSpec,false)

        mHeight = mWidth

        setMeasuredDimension(mWidth, mHeight)
    }

    private val DEFAULT_WIDTH: Int = 8.dip2px()
    private val DEFALUT_HEIGHT: Int = 8.dip2px()
    fun getMeasureSize(length: Int, isWidth: Boolean): Int {
        val mode = MeasureSpec.getMode(length)
        val size = MeasureSpec.getSize(length)
        var resultSize = 0
        var padding = if (isWidth) paddingLeft + paddingRight else paddingTop + paddingBottom
        when(mode){
            MeasureSpec.EXACTLY -> {
                resultSize = if (isWidth) DEFAULT_WIDTH + padding else DEFALUT_HEIGHT + padding
                resultSize = Math.max(resultSize, size)

            }

            else -> {
                resultSize = if (isWidth) DEFAULT_WIDTH + padding else DEFALUT_HEIGHT + padding
                if (mode == MeasureSpec.UNSPECIFIED) {
                    resultSize = Math.min(resultSize,size)
                }
            }
        }

       return resultSize
    }


}


//fun Int.dip2px():Int{
//    var scale = MyApplication.appContext.resources.displayMetrics.density
//    return (this* scale + 0.5f).toInt()
//}

