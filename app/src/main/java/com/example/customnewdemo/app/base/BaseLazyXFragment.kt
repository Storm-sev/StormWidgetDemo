package com.example.customnewdemo.app.base

/**
 * androidx 下的fragment 懒加载模式
 *
 */
abstract class BaseLazyXFragment : BaseFragment() {


    private var isLoad = false

    override fun onResume() {
        super.onResume()

        if (!isLoad && !isHidden) {
            lazyLoadData()
            isLoad = true

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        isLoad = false
    }


    abstract fun lazyLoadData()
}