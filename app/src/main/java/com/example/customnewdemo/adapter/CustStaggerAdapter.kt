package com.example.customnewdemo.adapter

import android.content.Context
import android.media.Image
import android.media.JetPlayer
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.customnewdemo.R
import com.example.customnewdemo.common.holder.RvViewHolder
import com.example.customnewdemo.utils.LogUtils
import com.example.customnewdemo.utils.dip2px
import com.example.customnewdemo.widget.CustomImageView
import kotlin.math.log

class CustStaggerAdapter( var context: Context) : RecyclerView.Adapter<RvViewHolder>() {


    companion object{
        val TAG = "CustStaggerAdapter"
    }
    var mData: ArrayList<String> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        return RvViewHolder.create(
            context,
            R.layout.item_custom, parent
        )

    }

    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
      bindData(holder,position)
    }

    private fun bindData(holder: RvViewHolder, position: Int) {
        var imageview = holder.getView<CustomImageView>(R.id.iv_img)

        var  width  = 0
        imageview.height = 150.dip2px()

       width =  imageview.width

//        imageview.post {
//            width = imageview.measuredWidth
//            LogUtils.d(TAG, "run 获取的宽度 $width")
//
//            val layoutParams = imageview.layoutParams
//            layoutParams.height = width * 3
//            imageview.layoutParams = layoutParams
//        }

//获取的高度


        LogUtils.d(TAG,"获取 image 的 宽度  ${imageview.measuredWidth}")

    }

    override fun getItemCount(): Int  = mData.size

    public fun refreshData(list: ArrayList<String>?) {
        list?.let {
            mData.clear()
            mData.addAll(list)
            notifyDataSetChanged()
        }

    }




}