package com.example.customnewdemo.utils


fun Int.sp2px(): Int {
    val scale = AppUtils.appContext.resources.displayMetrics.scaledDensity
    return (this * scale + 0.5f).toInt()
}


fun Int.px2sp(): Int {
    val scale = AppUtils.appContext.resources.displayMetrics.scaledDensity
    return (this / scale + 0.5f).toInt()
}


fun Int.dip2px(): Int {
    val scale = AppUtils.appContext.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Int.px2dip(): Int {
    val scale = AppUtils.appContext.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}


fun Float.sp2px(): Int {
    val scale = AppUtils.appContext.resources.displayMetrics.scaledDensity
    return (this * scale + 0.5f).toInt()
}


fun Float.px2sp(): Int {
    val scale = AppUtils.appContext.resources.displayMetrics.scaledDensity
    return (this / scale + 0.5f).toInt()
}


fun Float.dip2px(): Int {
    val scale = AppUtils.appContext.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Float.px2dip(): Int {
    val scale = AppUtils.appContext.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

