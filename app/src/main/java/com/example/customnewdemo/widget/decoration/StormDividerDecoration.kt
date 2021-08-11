package com.example.customnewdemo.widget.decoration

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StormDividerDecoration : RecyclerView.ItemDecoration {

    companion object{
        val TAG  = "StormDividerDecoration->"

        val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }


     var mOrientation: Int? = null
    var space: Int? = null
    private lateinit var paint:Paint

    constructor(space: Int, color: Int,orientation:Int){
        this.mOrientation = orientation
        this.space = space;
        paint = Paint()
        paint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            this.color = color
        }
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
//        super.getItemOffsets(outRect, view, parent, state)
        space?.let {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0,0,0,it)
            } else {
                outRect.set(0, 0, it, 0)

            }
        }
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c,parent)
        }else{
            drawHorizontal(c,parent)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        var top = parent.paddingTop
        var bottom= parent.height - parent.paddingBottom

        for (i in 0..childCount) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams

            var left = child.right + layoutParams.rightMargin
            var right = left+ space!!
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)

        }


    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {

        val childCount = parent.childCount

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0..childCount) {
            val child = parent.getChildAt(i)
            val recyclerView = RecyclerView(parent.context)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams

            var top = child.bottom + layoutParams.bottomMargin
            var  bottom = top + space!!

            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)


        }

    }


}