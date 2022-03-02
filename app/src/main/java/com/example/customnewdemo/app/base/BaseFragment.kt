package com.example.customnewdemo.app.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.customnewdemo.databinding.FragmentBaseBinding

abstract class BaseFragment : Fragment() {

    lateinit var baseBinding: FragmentBaseBinding
    protected var mStatusBarView: View? = null
    protected var mNavigatorView: View? = null
    protected lateinit var mContainerLayout: ContentStateLayout

    protected lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseBinding = FragmentBaseBinding.inflate(inflater,container,false)
        mContainerLayout = initNormalView();
        baseBinding.flContainer.addView(mContainerLayout)
        return baseBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _setStatusBarView()
        _setNavigatorView()
        setUpView()
        setUpListener()
        setUpData()

    }




    private fun initNormalView(): ContentStateLayout {
        return object : ContentStateLayout(mContext) {
            override fun detailClickOther() {
                detailOtherState()

            }

            override fun againLoad() {
                setUpData()
            }

            override fun initNormalView(): ViewGroup? {

                return attachLayoutRes() as ViewGroup
            }

        }
    }






    public abstract fun attachLayoutRes(): View

    private fun setUpData() {


    }

    /**
     * 处理其他状态的事情
     */
    private fun detailOtherState() {


    }

    private fun _setNavigatorView() {
        if (getNavigatorView() != 0 && null == mNavigatorView) {

            baseBinding.vsNavigator.layoutResource = getNavigatorView()
            mNavigatorView = baseBinding.vsNavigator.inflate()
        }
    }


    /**
     * 顶部状态栏膨胀
     */
    private fun _setStatusBarView() {
        if (getStatusBarView() != 0 && null == mStatusBarView) {

            baseBinding.vsStatusBar.layoutResource = getStatusBarView()
            mStatusBarView = baseBinding.vsStatusBar.inflate()
        }
    }

    abstract fun setUpListener()

    abstract fun setUpView()


    /**
     * 顶部 标题栏添加  layout res
     */
    abstract fun getNavigatorView(): Int


    /**
     * 底部导航栏的添加
     *
     */
    abstract fun getStatusBarView(): Int

}