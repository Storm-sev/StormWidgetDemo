package com.example.customnewdemo.utils

import android.content.Context
import android.os.Build
import android.view.ViewConfiguration
import java.lang.reflect.Method

object ScreenUtils {


    /**
     * 获取底部导航栏高度
     */
    public fun getNavigatorBarHeight(context: Context): Int {
        var result = 0;
        if (hasNacBar(context)) {
            var res = context.resources

            val resId = res.getIdentifier("navigator_bar_height", "dimen", "android")
            if (resId > 0) {
                result = res.getDimensionPixelSize(resId)
            }
        }

        return result
    }

    private fun hasNacBar(context: Context): Boolean {

        val res = context.resources
        val resourceId =
            context.resources.getIdentifier("config_showNavigationbar", "bool", "android")

        if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId)
            var sNavBarOverride = getNavBarOverride()
            sNavBarOverride?.let {
                if (it.equals("1")) {
                    hasNav = false
                } else if (it.equals("0")) {
                    hasNav = true
                }
            }

            return hasNav
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey()
        }
    }

    /**
     * 判断虚拟按键是否被重写
     */
    private fun getNavBarOverride(): String? {

        var sNavBarOverride : String? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                var c = Class.forName("android.os.SystemProperties")
                var method: Method = c.getDeclaredMethod("get", String::class.java)
                method.isAccessible = true
                sNavBarOverride = method.invoke(null, "qemu.hw.mainkeys") as String

            } catch (t: Throwable) {
                t.printStackTrace()
            }

        }

        return sNavBarOverride

    }
}