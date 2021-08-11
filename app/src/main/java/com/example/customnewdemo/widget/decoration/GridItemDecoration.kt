package com.example.customnewdemo.widget.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration : RecyclerView.ItemDecoration {


    companion object{
        val TAG = "GridItemDecoration->"
    }

    var space: Int? = null
    var spanCount: Int = 3
    var includeEdge: Boolean? = null


    constructor(space: Int, spanCount: Int, includeEdge: Boolean) {
        this.space = space
        this.spanCount = spanCount
        this.includeEdge = includeEdge

    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
//        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        includeEdge?.let {
            if (it) {

                outRect.left = space!! - column * space!! / spanCount
                outRect.right = (column + 1) * space!! / spanCount

                if (position < spanCount) {
                    outRect.top = space!!
                }
                outRect.bottom = space!!

            } else {
                outRect.left = column * space!! / spanCount
                outRect.right = space!! - (column + 1) * space!! / spanCount

                if (position >= spanCount) {
                    outRect.top = space!!
                }
            }
        }

    }


}