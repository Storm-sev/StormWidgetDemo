package com.example.customnewdemo.widget

import android.content.Context
import android.graphics.*

import android.util.AttributeSet
import android.view.View
import com.example.customnewdemo.utils.dip2px


import com.example.customnewdemo.R
import com.example.customnewdemo.utils.sp2px


class SpiderView : View {

    private lateinit var mContext: Context;

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var edges: Int = 5; // n边形的边数
    private var degress: Double = 360.toDouble() / edges

    private var hudu: Double = (Math.PI / 180) * degress


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        this.mContext = context;
        attrs?.let {
            config(context, it)
        }

    }

    private var spiderBackground: Int? = null
    private var normalLineColor: Int? = null
    private var userLineColor: Int? = null
    private var polygonOutSideColor: Int? = null
    private var polygonInSideColor: Int? = null
    private var polygonInSideLineColor: Int? = null // 连接线
    private var spiderNormalTextColor: Int? = null
    private var spiderAverageTextColor: Int? = null
    private var spiderUserValueTextColor: Int? = null
    private var normalTextSize: Float? = null
    private var spiderAverageOrUserTextSize: Float? = null
    private var spiderRadius = 0f;
    private var radiusStep: Float = 0f;
    private var lineFillColor: Int? = null


    private fun config(context: Context, attrs: AttributeSet) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpiderView)

        try {
            spiderBackground = typedArray.getColor(
                R.styleable.SpiderView_spider_background,
                Color.parseColor("#334446f2")
            )
            normalLineColor = typedArray.getColor(
                R.styleable.SpiderView_normal_line_color,
                Color.parseColor("#FFD342")
            )
            userLineColor = typedArray.getColor(
                R.styleable.SpiderView_user_line_color,
                Color.parseColor("#A7EBFF")
            )
            polygonOutSideColor = typedArray.getColor(
                R.styleable.SpiderView_polygon_outside_color,
                Color.parseColor("#80FFFFFF")
            )
            polygonInSideColor = typedArray.getColor(
                R.styleable.SpiderView_polygon_inside_color,
                Color.parseColor("#4DFFFFFF")
            )
            polygonInSideLineColor = typedArray.getColor(
                R.styleable.SpiderView_polygon_inside_line_color,
                Color.parseColor("#A7EBFF")
            )
            spiderNormalTextColor =
                typedArray.getColor(R.styleable.SpiderView_spider_normal_textcolor, Color.WHITE)
            spiderAverageTextColor = typedArray.getColor(
                R.styleable.SpiderView_spider_average_textcolor,
                Color.parseColor("#FFD342")
            )
            spiderUserValueTextColor = typedArray.getColor(
                R.styleable.SpiderView_spider_user_value_textcolor,
                Color.parseColor("#66D5FF")
            )
            lineFillColor = typedArray.getColor(
                R.styleable.SpiderView_spider_fill_line_color,
                Color.parseColor("#CC4446F2")
            )
            normalTextSize = typedArray.getDimension(
                R.styleable.SpiderView_spider_normal_textsize,
                12.sp2px().toFloat()
            )
            spiderAverageOrUserTextSize = typedArray.getDimension(
                R.styleable.SpiderView_spider_average_or_use_textsize,
                10.sp2px().toFloat()
            )
            radiusStep = typedArray.getDimension(
                R.styleable.SpiderView_spider_radius_step,
                15.sp2px().toFloat()
            )
            spiderRadius =
                typedArray.getDimension(R.styleable.SpiderView_spider_radius, 85.dip2px().toFloat())


        } finally {
            typedArray.recycle()
        }


    }


    private val edgePaint: Paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.dip2px().toFloat()
        paint.color = polygonOutSideColor!!

        paint
    }

    private val edgeLinePaint: Paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.dip2px().toFloat()
        paint.color = polygonInSideColor!!

        paint
    }

    private val normalRanksPaint: Paint by lazy {

        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.dip2px().toFloat()
        paint.color = normalLineColor!!
        val pathEffect: PathEffect = DashPathEffect(floatArrayOf(10f, 5f), 10f)
        paint.setPathEffect(pathEffect)
        paint
    }

    private val rankPaint: Paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.dip2px().toFloat()
        paint.color = userLineColor!!
//        val shader: Shader = LinearGradient(
//            100, 100, 500, 500, Color.parseColor("#E91E63"),
//            Color.parseColor("#2196F3"), Shader.TileMode.CLAMP
//        )
        paint
    }

    private val rankFillPaint: Paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = lineFillColor!!



        paint
    }

    private val pointPaint: Paint by lazy {

        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND

        paint.strokeWidth = 5.dip2px().toFloat()

        paint
    }


    private val bgPaint: Paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = spiderBackground!!
        paint
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawPolygon(canvas!!, spiderRadius, polygonOutSideColor!!)
        drawPolygon(canvas!!, spiderRadius - radiusStep, polygonInSideColor!!)
        drawPolygon(canvas!!, spiderRadius - radiusStep * 2, polygonInSideColor!!)
        drawLine(canvas!!, spiderRadius)
        drawNormalRanks(canvas!!, spiderRadius)
        drawRanks(canvas!!, spiderRadius)
        drawText(canvas!!, spiderRadius)

    }


    private val normalTextPaint: Paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true

        paint
    }

    private val normalText: Array<String> =
        arrayOf<String>("一次通过率", "市场占有率", "综合评价得分", "负面舆情占比", "三年违法率")

    private val aveOrActData: Array<String?> = arrayOfNulls<String>(edges)


    private fun setContentValue(aveArray: IntArray, actArray: IntArray) {

        for (i in 0 until edges) {
            aveOrActData[i] = "平均值${aveArray[i]}%本驾校${actArray[i]}%"
        }



    }
    private fun drawText(canvas: Canvas, radius: Float) {

        var endX: Float = 0f;
        var endY: Float = 0f;
        var aveX: Float = 0f;
        var aveY: Float = 0f;

        val rect = Rect()
        val aveRect = Rect()



        for (i in 0 until edges) {
            //绘制主体
            normalTextPaint.color = spiderNormalTextColor!!
            normalTextPaint.textSize = normalTextSize!!


            normalTextPaint.getTextBounds(normalText[i], 0, normalText[i].length, rect)
            endX =
                (mWidth / 2 + radius * 1.5 * Math.sin(hudu * i) - (rect.right - rect.left) / 2).toFloat()
            endY =
                (mHeight / 2 - radius * 1.3 * Math.cos(hudu * i) + (rect.bottom - rect.top) / 2).toFloat()

            canvas.drawText(normalText[i], endX, endY, normalTextPaint)

            if (aveOrActData[0] == null) {

                setContentValue(intArrayOf(0, 0, 0, 0, 0), intArrayOf(0, 0, 0, 0, 0))
            }
            normalTextPaint.textSize = spiderAverageOrUserTextSize!!

            normalTextPaint.getTextBounds(aveOrActData[i], 0, aveOrActData[i]!!.length, aveRect)

            aveX = endX - (aveRect.width() - rect.width() + 4f.dip2px()) / 2

            aveY = endY + (aveRect.bottom - aveRect.top) + 2f.dip2px()

            normalTextPaint.color = spiderAverageTextColor!!
            canvas.drawText(
                aveOrActData[i]!!,
                0,
                aveOrActData[i]!!.length / 2,
                aveX,
                aveY,
                normalTextPaint
            )
            normalTextPaint.color = spiderUserValueTextColor!!
            canvas.drawText(
                aveOrActData[i]!!,
                aveOrActData[i]!!.length / 2,
                aveOrActData[i]!!.length,
                aveX + aveRect.width() / 2 + 4f.dip2px(),
                aveY,
                normalTextPaint
            )


        }
    }


    private var normalRankData: FloatArray = floatArrayOf()

    private var rankData: FloatArray = floatArrayOf()

// private var normalRankData: FloatArray = null
//    private var rankData: FloatArray = floatArrayOf(0.5f, 0.6f, 0.8f, 0.4f, 0.5f)


    private fun setNormalRank(normalRankData: FloatArray, rankData: FloatArray) {

        this.normalRankData = normalRankData
        this.rankData = rankData
        invalidate()

    }




    private fun drawRanks(canvas: Canvas, radius: Float) {
        var path = Path()
        var endX: Float = 0f;
        var endY: Float = 0f;
        pointPaint.color = userLineColor!!

        if (rankData.size == 0) {
            return
        }
        for (i in 0 until edges) {

            endX = width.toFloat() / 2 + radius * Math.sin(hudu * i).toFloat() * rankData[i]

            endY =
                height.toFloat() / 2 - radius * Math.cos(hudu * i).toFloat() * rankData[i]

            if (i == 0) {
                path.moveTo(
                    mWidth.toFloat() / 2,
                    mHeight.toFloat() / 2 - radius * rankData[0]
                )
//                canvas.drawPoint(
//                    mWidth.toFloat() / 2,
//                    mHeight.toFloat() / 2 - radius * rankData[0],
//                    pointPaint
//                )
            } else {
//                canvas.drawPoint(endX, endY, pointPaint)

                path.lineTo(endX, endY)
            }

        }


        path.close()
        canvas.drawPath(path, rankFillPaint)
        canvas.drawPath(path, rankPaint)


        for (i in 0 until edges) {

            endX = width.toFloat() / 2 + radius * Math.sin(hudu * i).toFloat() * rankData[i]

            endY =
                height.toFloat() / 2 - radius * Math.cos(hudu * i).toFloat() * rankData[i]

            if (i == 0) {

                canvas.drawPoint(
                    mWidth.toFloat() / 2,
                    mHeight.toFloat() / 2 - radius * rankData[0],
                    pointPaint
                )
            } else {
                canvas.drawPoint(endX, endY, pointPaint)

            }

        }

    }

    private fun drawNormalRanks(canvas: Canvas, radius: Float) {
        var path = Path()
        var endX: Float = 0f;
        var endY: Float = 0f;

        if (normalRankData.size == 0) {
            return
        }
        pointPaint.color = normalLineColor!!
        for (i in 0 until edges) {

            endX = width.toFloat() / 2 + radius * Math.sin(hudu * i).toFloat() * normalRankData[i]

            endY =
                height.toFloat() / 2 - radius * Math.cos(hudu * i).toFloat() * normalRankData[i]

            if (i == 0) {
                path.moveTo(
                    mWidth.toFloat() / 2,
                    mHeight.toFloat() / 2 - radius * normalRankData[0]
                )
                canvas.drawPoint(
                    mWidth.toFloat() / 2,
                    mHeight.toFloat() / 2 - radius * normalRankData[0],
                    pointPaint
                )
            } else {
                canvas.drawPoint(endX, endY, pointPaint)

                path.lineTo(endX, endY)
            }


        }
        path.close()
        canvas.drawPath(path, normalRanksPaint)
    }


    private fun drawLine(canvas: Canvas, radius: Float) {

        var path = Path()
        path.moveTo(mWidth.toFloat() / 2, mHeight.toFloat() / 2)
        var endX: Float = 0f;
        var endY: Float = 0f;

        for (i in 0 until edges) {

            endX = (width.toFloat() / 2 + radius * Math.sin(hudu * i)).toFloat()

            endY = (height.toFloat() / 2 - radius * Math.cos(hudu * i)).toFloat()

            path.lineTo(endX, endY)
            canvas.drawPath(path, edgeLinePaint)
            endX = mWidth.toFloat() / 2
            endY = mHeight.toFloat() / 2
            path.moveTo(endX, endY)

        }


    }

    private fun drawPolygon(canvas: Canvas, radius: Float, color: Int) {
        var path = Path()
        path.moveTo(mWidth.toFloat() / 2, mHeight.toFloat() / 2 - radius)
        var endX: Float = 0f;
        var endY: Float = 0f;

        for (i in 0 until edges) {

            endX = (width.toFloat() / 2 + radius * Math.sin(hudu * i)).toFloat()

            endY = (height.toFloat() / 2 - radius * Math.cos(hudu * i)).toFloat()

            path.lineTo(endX, endY)

        }
        path.close()
        edgePaint.color = color
        canvas.drawPath(path, edgePaint)
        canvas.drawPath(path, bgPaint)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = getMeasureSize(widthMeasureSpec, true)
        mHeight = getMeasureSize(heightMeasureSpec, false)
        setMeasuredDimension(mWidth, mHeight)
    }


    private val DEFAULT_WIDTH: Int = 8.dip2px()
    private val DEFALUT_HEIGHT: Int = 8.dip2px()

    fun getMeasureSize(length: Int, isWidth: Boolean): Int {
        val mode = MeasureSpec.getMode(length)
        val size = MeasureSpec.getSize(length)
        var resultSize = 0
        var padding = if (isWidth) paddingLeft + paddingRight else paddingTop + paddingBottom
        when (mode) {
            MeasureSpec.EXACTLY -> {
                resultSize = if (isWidth) DEFAULT_WIDTH + padding else DEFALUT_HEIGHT + padding
                resultSize = Math.max(resultSize, size)

            }

            else -> {
                resultSize = if (isWidth) DEFAULT_WIDTH + padding else DEFALUT_HEIGHT + padding
                if (mode == MeasureSpec.UNSPECIFIED) {
                    resultSize = Math.min(resultSize, size)
                }
            }
        }

        return resultSize
    }


    public fun setEdge(edges: Int) {
        this.edges = edges
        this.degress =  360.toDouble() / edges
        this.degress =  (Math.PI / 180) * degress
    }
}