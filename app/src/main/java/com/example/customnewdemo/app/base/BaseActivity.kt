package com.example.customnewdemo.app.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity

import com.example.customnewdemo.databinding.ActivityBaseBinding


public abstract class BaseActivity<P : IPresenter> : AppCompatActivity() {

    lateinit var baseBinding: ActivityBaseBinding
    protected var mStatusBarView: View? = null
    protected var mNavigatorView: View? = null
    protected lateinit var mContainerLayout: ContentStateLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setStateBarState()
        _setStatusBarView()
        _setNavigatorView()
        mContainerLayout = initNormalView()
        baseBinding.flContainer.addView(mContainerLayout)
        setContentView(baseBinding.root)
        setUpView()
        setUpAdapter();
        setUpData()
        setUpListener()


    }

    abstract fun setUpListener()

    private fun setUpAdapter() {

    }

    abstract fun setUpView()

    private fun initNormalView(): ContentStateLayout {
        return object : ContentStateLayout(this) {
            override fun detailClickOther() {
                detailOtherStateAny()
            }

            override fun againLoad() {
                setUpData()
            }

            override fun initNormalView(): ViewGroup {
                return attachLayoutRes() as ViewGroup
            }

        }

    }


    protected abstract fun setUpData()

    /**
     * 其他状态下 点击控件 之后进行的操作逻辑
     */
    protected fun detailOtherStateAny() {

    }

    protected abstract fun attachLayoutRes(): View

    private fun _setNavigatorView() {
        if (setNavigatorView() == 0) {
            return
        }

        if (null == mNavigatorView) {
            baseBinding.vsNavigator.layoutResource = setNavigatorView()
            mNavigatorView = baseBinding.vsNavigator.inflate()
        }

    }


    private fun _setStatusBarView() {
        if (setStatusBarView() == 0) {
            return
        }

        if (null == mStatusBarView) {
            baseBinding.vsStatusBar.layoutResource = setStatusBarView()
            mStatusBarView = baseBinding.vsStatusBar.inflate()
        }
    }

    abstract fun setStatusBarView(): Int
    abstract fun setNavigatorView(): Int


    // 更新顶部状态栏的状态

    protected abstract fun setStateBarState()


}