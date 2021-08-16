package com.example.customnewdemo.app.base

import android.os.Bundle
import android.view.View

open class BaseLazyFragment : BaseFragment() {


    companion object {
        val TAG = "BaseLazyFragment"
    }


    protected var isViewCreated: Boolean = false // 界面view 是否创建
    protected var isVisibleToUser: Boolean = false // 是否对用户可见
    protected var isDataLoad: Boolean = false // 首次数据是否加载
    protected var hiddentState: Boolean = true // fragment 是否隐藏状态


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isViewCreated = true
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vpLazyLoadData()
        lazyTryLoadData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        hiddentState = hidden
    }

    public fun isNeedReload(): Boolean {
        return false
    }


    /**
     * show hide 场景 .
     */
    private fun lazyTryLoadData() {

        if (!isParentHidden() && !hiddentState && (isNeedReload() || !isDataLoad)) {
            setupLazyData()
            isDataLoad = true
            dispatchParentHiddenState()
        }


    }


    private fun setupLazyData() {


    }

    private fun vpLazyLoadData() {

        if (isViewCreated && isVisibleToUser && isParentVisible() && (isNeedReload() || !isDataLoad)) {
            setupLazyData()
            isDataLoad = true
            dispatchParentVisibleState()
        }

    }

    private fun dispatchParentVisibleState() {

        val fragments = childFragmentManager.fragments

        if (fragments.isEmpty()) {
            return
        }

        for (child in fragments) {
            if (child is BaseLazyFragment && (child as BaseLazyFragment).isVisibleToUser) {
                (child as BaseLazyFragment).vpLazyLoadData()
            }
        }
    }


    /**
     * 判断 父fragment的状态
     */
    public fun isParentVisible(): Boolean {
        var fragment = parentFragment

        return fragment == null || (fragment is BaseFragment && (fragment as BaseLazyFragment).isVisibleToUser)

    }

    /**
     * fragment parent  hiddent 隐藏 和显示
     */
    public fun isParentHidden(): Boolean {
        var fragment = parentFragment
        if (null == fragment) {
            return false
        }

        if (fragment is BaseLazyFragment && !((fragment as BaseLazyFragment).hiddentState)) {
            return false
        }
        return true
    }


    private fun dispatchParentHiddenState() {

        val fragments = childFragmentManager.fragments

        if (fragments.isEmpty()) {
            return
        }

        for (child in fragments) {
            if (child is BaseLazyFragment && (child as BaseLazyFragment).hiddentState) {
                (child as BaseLazyFragment).lazyTryLoadData()
            }
        }

    }

    override fun onDestroy() {
        isViewCreated = false
        isVisibleToUser = false
        isDataLoad = false
        hiddentState = true
        super.onDestroy()

    }


}