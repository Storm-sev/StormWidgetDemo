package com.example.customnewdemo.act

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.customnewdemo.app.login.LoginContext
import com.example.customnewdemo.databinding.ActivityLoginBinding
import com.example.customnewdemo.utils.LogUtils
import com.example.customnewdemo.utils.BarUtils

/**
 * 登录模块测试
 */
class LoginActivity : AppCompatActivity() {

    companion object {
        val TAG = "LoginActivity"

    }

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 获取登录状态
        val instance = LoginContext.getInstance()
        keepLoginInBtnNotOver(binding.rootMian, binding.btnLogin)

        var hasNacBar = BarUtils.isNavBarVisible(window)
        LogUtils.d(TAG, "虚拟按键   --> $hasNacBar")

        val navBarHeight = BarUtils.getNavBarHeight(this)
        LogUtils.d(TAG, "获取底部导航的高度  --> $navBarHeight")

        setUpListener()

    }

    private fun setUpListener() {
        binding.tvLogin.setOnClickListener {
            LoginContext.getInstance().login()

        }
        binding.tvLoginOut.setOnClickListener {
            LoginContext.getInstance().loginOut()
        }

        binding.tvPay.setOnClickListener {
            LoginContext.getInstance().pay()
        }

        binding.tvCollect.setOnClickListener {
            LoginContext.getInstance().collect()

        }
    }


    /**
     * 保持登录按钮 不被遮挡
     *
     */
    public fun keepLoginInBtnNotOver(root: View, subView: View) {


        root.viewTreeObserver.addOnGlobalLayoutListener {

            val rect = Rect()
            // 获取 root 的可视区域
            root.getWindowVisibleDisplayFrame(rect)

            // root  不可视区域
            var rootInvisibleHeight = root.rootView.height - rect.bottom

            LogUtils.d(TAG, "不可视区域 高度  -> $rootInvisibleHeight")
            //此处 location 需要考虑顶部状态栏的高度
            var location = IntArray(2)
            subView.getLocationOnScreen(location)
            var loginBtnBottom = location[1] - BarUtils.getStatusBarHeight(root.context)

            if (rootInvisibleHeight > 200) {
                //显示键盘 情况下 滑动布局
                var scrollHeight =
                    rootInvisibleHeight - (root.height - loginBtnBottom - subView.height) -
                            if (BarUtils.isNavBarVisible(this)) BarUtils.getNavBarHeight(this) else 0
                LogUtils.d(TAG, "获取view 的高度  + ${subView.height}")
                LogUtils.d(TAG, "获取底部导航栏的高度 -- ${BarUtils.getNavigatorBarHeight(root.context)}")

                LogUtils.d(TAG, "需要滚动的 高度 -- $scrollHeight")
                if (scrollHeight > 0) {
                    root.scrollTo(
                        0,
                        Math.abs(scrollHeight)
                    )

                }
            } else {
                root.scrollTo(0, 0)

            }

        }
    }
}