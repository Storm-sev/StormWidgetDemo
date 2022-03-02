package com.example.customnewdemo.app.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ContentStateAllBinding


abstract class ContentStateLayout : FrameLayout {


    private var mContext: Context = context
    private lateinit var binding: ContentStateAllBinding
    lateinit var mRootView: View
    private var mNormalView: ViewGroup? = null
    private var mLoadingView: View? = null
    private var mNoDataView: View? = null
    private var mOtherStateView: View? = null
    private var mNetWorkErrorView: View? = null


    constructor(context: Context) : super(context) {

        mRootView = inflate(context, R.layout.content_state_all, this)
        binding = ContentStateAllBinding.bind(mRootView)

        // 初始化 normalview
        mNormalView = initNormalView()

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT

        )

        addView(mNormalView, layoutParams)

        setUpListener()
    }


    public fun changePageState(pageState: PageState, clickId: Int) {
        when (pageState) {
            PageState.NORMAL -> {
                mNormalView?.visibility = VISIBLE
                mLoadingView?.visibility = GONE
                mNoDataView?.visibility = GONE
                mNetWorkErrorView?.visibility = GONE
                mOtherStateView?.visibility = GONE
                invalidate()
            }
            PageState.NO_DATA -> {
                mNormalView?.visibility = GONE
                mLoadingView?.visibility = GONE
                mNetWorkErrorView?.visibility = GONE
                mOtherStateView?.visibility = GONE

                if (mNoDataView == null) {
                    initNoDataView(0)
                } else {
                    mNoDataView?.visibility = VISIBLE
                }

                var clickView =
                    mNoDataView?.findViewById<View>(if (id == 0) R.id.def_no_data else id)

                clickView?.setOnClickListener { view ->
                    {
                        loadDataAgain()
                    }
                }
            }

            PageState.LOADING -> {
                mNormalView?.visibility = GONE
                mNoDataView?.visibility = GONE
                mNetWorkErrorView?.visibility = GONE
                mOtherStateView?.visibility = GONE

                if (null == mLoadingView) initLoadingView(0) else mLoadingView?.visibility = VISIBLE

            }

            PageState.NETWORK_ERROR -> {
                mNormalView?.visibility = GONE
                mNoDataView?.visibility = GONE
                mLoadingView?.visibility = GONE
                mOtherStateView?.visibility = GONE

                if (null == mNetWorkErrorView) initNetWorkErrorView(0) else mNetWorkErrorView?.visibility =
                    VISIBLE

                val clickView =
                    mNetWorkErrorView?.findViewById<View>(if (id == 0) R.id.def_net_error else id)
                clickView?.setOnClickListener {
                    loadDataAgain()
                }


            }

            PageState.OTHER_STATE -> {
                mNormalView?.visibility = GONE
                mNoDataView?.visibility = GONE
                mLoadingView?.visibility = GONE
                mNetWorkErrorView?.visibility = GONE

                if (null == mOtherStateView) initOtherStateView(0) else mOtherStateView!!.visibility =
                    VISIBLE

                val clickView =
                    mOtherStateView?.findViewById<View>(if (id == 0) R.id.def_other_state else id)

                clickView?.setOnClickListener {
                    detailClickOtherState()
                }
            }
        }
    }

    /**
     * 当页面设置了其他状态, 需要哟点击去处理其他状态的流程
     */
    private fun detailClickOtherState() {
        detailClickOther()
    }

    /**
     * 其他状态下自定义处理点击逻辑
     */
    abstract fun detailClickOther()


    /**
     * 加载失败 再次加载数据(使用页面no data) 时候的再次调用
     */
    private fun loadDataAgain() {
        againLoad()
    }

    // 再次加载数据
    abstract fun againLoad()

    public var loadingBinding = null

    private fun setUpListener() {

        binding.vsLoadingStub.setOnInflateListener { _, inflateId ->
            {

            }
        }
    }


    public fun initOtherStateView(@LayoutRes layoutRes: Int) {

        if (null != mOtherStateView) return
        binding.vsOtherStateStub.layoutResource =
            if (layoutRes == 0) R.layout.layout_def_other_state else layoutRes
        mOtherStateView = binding.vsOtherStateStub.inflate()

    }


    public fun initNetWorkErrorView(@LayoutRes layoutRes: Int) {
        if (null != mNetWorkErrorView) return
        binding.vsNetworkErrorStub.layoutResource =
            if (layoutRes == 0) R.layout.layout_def_network_error_state else layoutRes
        mNetWorkErrorView = binding.vsNetworkErrorStub.inflate()

    }

    public fun initNoDataView(@LayoutRes layoutRes: Int) {
        if (null != mNoDataView) return
        binding.vsNoDataStub.layoutResource =
            if (layoutRes == 0) R.layout.layout_def_no_data_state else layoutRes
        mNoDataView = binding.vsNoDataStub.inflate()
    }


    public fun initLoadingView(@LayoutRes layoutRes: Int) {
        if (null != mLoadingView) return
        binding.vsLoadingStub.layoutResource =
            if (layoutRes == 0) R.layout.layout_default_loading_state else layoutRes
        mLoadingView = binding.vsLoadingStub.inflate()
    }

    abstract fun initNormalView(): ViewGroup?


    public enum class PageState {

        NORMAL, NO_DATA, LOADING, NETWORK_ERROR, OTHER_STATE

    }

}