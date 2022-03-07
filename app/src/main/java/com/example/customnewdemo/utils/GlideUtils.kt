package com.example.customnewdemo.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

object GlideUtils {


    public fun loadImage(context: Context, url: String, imageView: ImageView) {

        Glide.with(context)
            .load(url)
            .into(imageView)

    }
}

// 扩展函数
fun ImageView.loadImg(context: Context, url: String) {
    Glide.with(context).load(url).into(this)
}
