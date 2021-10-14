package com.example.customnewdemo.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.DisplayCutout
import android.view.ViewConfiguration

import java.lang.reflect.Method



object BarUtils {


    val TAG = "BarUtils"


    /**
     * 判断是否为刘海屏
     */
    public fun hasNotchScreen(context: Context): Boolean {

        return hasNotchMIUI("ro.miui.notch", context) ||
                hasNotchHUAWEI(context) ||
                hasNotchAtOppo(context) ||
                hasNotchAtVIVO(context)
    }

    /**
     * 获取刘海屏的高度
     */
    public fun getHeight(activity: Activity): Float {
        val dm = activity.resources.displayMetrics
        if (hasNotchScreen(activity)) {
            var notchHeight = 0F
            if (isHuaWei()) {
                notchHeight = getHUAWEINotchSize(activity)[1].toFloat()
            } else if (isXiaoMi()) {
                notchHeight = getXIAOMINotchHeight(activity).toFloat()
            } else if (isVivo()) {
                notchHeight = getXIAOMINotchHeight(activity).toFloat()
            } else if (isOppo()) {
                notchHeight = getXIAOMINotchHeight(activity).toFloat()
            }
            return dm.heightPixels + getNavigatorBarHeight(activity) - notchHeight
        }
        return dm.heightPixels + getNavigatorBarHeight(activity).toFloat()
    }

    /**
     * android p 的刘海屏判断
     */
    public fun isAndroidP(activity: Activity): DisplayCutout? {
        var decorView = activity.window.decorView
        if (null != decorView && android.os.Build.VERSION.SDK_INT >= 28) {
            val rootWindowInsets = decorView.rootWindowInsets
            if (null != rootWindowInsets) {
                return rootWindowInsets.displayCutout
            }
        }
        return null
    }

    /**
     * 获取小米手机的高度
     */
    public fun getXIAOMINotchHeight(activity: Activity): Int {
        val resId = activity.resources.getIdentifier("notch_height", "dimen", "android")
        if (resId > 0) {
            return activity.resources.getDimensionPixelSize(resId)
        }
        return 0
    }


    /**
     * oppo 是否有刘海屏
     */
    public fun hasNotchAtOppo(context: Context): Boolean {
        return context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    // vivo
    val VIVO_NOTCH: Int = 0x00000020  // 是否有刘海
    val VIVO_FILLET: Int = 0x00000008 // 是否圆角

    /**
     * vivo  是否有刘海屏
     */
    public fun hasNotchAtVIVO(context: Context): Boolean {
        var result: Boolean = false
        try {

            val classLoader = context.classLoader
            val ftFeature = classLoader.loadClass("android.util.FtFeature")
            val method = ftFeature.getMethod("isFeatureSupport", Int::class.java)
            result = method.invoke(ftFeature, VIVO_NOTCH) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.d(TAG, "hasNotchAtVIVO -> ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.d(TAG, "hasNotchAtVIVO -> NoSuchMethodException")
        } catch (e: Exception) {
            Log.d(TAG, "hasNotchAtVIVO -> Exception")
        } finally {
            return result
        }
    }

    /**
     * 华为手机获取刘海屏尺寸
     *
     */
    public fun getHUAWEINotchSize(context: Context): IntArray {
        var result = intArrayOf(0, 0)

        try {
            val classLoader = context.classLoader
            val loadClass = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val method = loadClass.getMethod("getNotchSize")

            result = method.invoke(loadClass) as IntArray

        } catch (e: Throwable) {
            Log.d(TAG, "getNotchSize Exception")

        }
        return result

    }

    /**
     * 华为 刘海屏判断
     */
    public fun hasNotchHUAWEI(context: Context): Boolean {
        var result: Boolean = false
        try {
            val classLoader = context.classLoader
            val loadClass = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val method = loadClass.getMethod("hasNotchInScreen")
            result = method.invoke(loadClass) as Boolean

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()

        } catch (e: NoSuchMethodError) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return result

        }
    }

    /**
     * 小米手机 是否为刘海屏
     */
    public fun hasNotchMIUI(key: String, context: Context): Boolean {
        var result = 0;
        if (isXiaoMi()) {

            try {
                var classLoader = context.classLoader

                @SuppressWarnings("rawtypes")
                var SystemProperties = classLoader.loadClass("android.os.SystemProperties")
                //
                @SuppressWarnings("rawtypes")
                var paramTypes = arrayOfNulls<Class<*>>(2)


                paramTypes[0] = String::class.java
                paramTypes[1] = Int::class.java
                val getInt: Method =
                    SystemProperties.getMethod("getInt", paramTypes[0], paramTypes[1])

                //参数
                var params = arrayOfNulls<Any>(2)
                params[0] = key
                params[1] = 0
                result = getInt.invoke(SystemProperties, params) as Int


            } catch (e: Throwable) {
                e.printStackTrace()
            }

        }
        return result == 1

    }

    /**
     * 是否是小米手机
     */
    private fun isXiaoMi() = "Xiaomi".equals(Build.MANUFACTURER)

    /**
     * 华为
     */
    private fun isHuaWei() = "Huawei".equals(Build.MANUFACTURER)

    /**
     * oppo
     */
    private fun isOppo() = "Oppo".equals(Build.MANUFACTURER)

    /**
     * vivo
     */
    private fun isVivo() = "Vivo".equals(Build.MANUFACTURER)

    /**
     * 获取顶部状态栏高度
     */
    public fun getStatusBarHeight(context: Context): Int {

        var result: Int = 0;

        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resId > 0) {
            result = context.resources.getDimensionPixelSize(resId)

        }
        return result

    }

    /**
     * 获取底部导航栏高度
     */
    public fun getNavigatorBarHeight(context: Context): Int {
        var result = 0;
        if (hasNacBar(context)) {
            var res = context.resources

            var resId = res.getIdentifier("navigator_bar_height", "dimen", "android")
//            if (resId) {
            result = res.getDimensionPixelSize(resId)
//            }
        }

        return result
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private fun hasNacBar(context: Context): Boolean {

        val res = context.resources
        val resourceId =
            context.resources.getIdentifier("config_showNavigationBar", "bool", "android")

        if (resourceId != 0) {
            var hasNav: Boolean = res.getBoolean(resourceId)
            var sNavBarOverride: String? = getNavBarOverride()
            sNavBarOverride?.let {
                if (it == "1") {
                    hasNav = false
                } else if (it == "0") {
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

        var sNavBarOverride: String? = null

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