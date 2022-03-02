package com.example.customnewdemo.act


import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import com.example.customnewdemo.app.base.BaseActivity
import com.example.customnewdemo.R
import com.example.customnewdemo.act.fragment.CusFragment
import com.example.customnewdemo.databinding.ActivityCustomBaseBinding


class CustomBaseActivity : BaseActivity<CustomPresenter>() {



    override fun setUpListener() {
    }

    override fun setUpView() {

        mBinding.tvContent.text = "动态获取的数据"


        val bt = supportFragmentManager.beginTransaction()
        bt.add(R.id.fl_container, CusFragment())
        bt.commit()


    }

    override fun setUpData() {

    }

    lateinit var mBinding: ActivityCustomBaseBinding
    override fun attachLayoutRes(): ViewGroup {
        mBinding = ActivityCustomBaseBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun setStatusBarView(): Int = R.layout.cus_statusbar

    override fun setNavigatorView(): Int = R.layout.cus_navigator

    override fun setStateBarState() {

    }

    companion object {
        public fun startSelf(activity: Activity) {
            activity.startActivity(Intent(activity, CustomBaseActivity::class.java))

        }
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_custom_base)
//    }
}