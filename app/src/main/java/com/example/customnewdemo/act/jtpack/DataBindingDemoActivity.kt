package com.example.customnewdemo.act.jtpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ActivityDataBindingDemoBinding
import com.example.customnewdemo.jtpack.DataBindingViewModel
import com.example.customnewdemo.jtpack.UserInfo
import com.example.customnewdemo.utils.LogUtils
import org.jzvd.jzvideo.TAG

class DataBindingDemoActivity : AppCompatActivity(), OnClickListener {

//    lateinit var mBinding: ActivityDataBindingDemoBinding

    private lateinit var viewModel: DataBindingViewModel
    lateinit var dataBinding: ActivityDataBindingDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mBinding = ActivityDataBindingDemoBinding.inflate(layoutInflater)
//        setContentView(mBinding.root)


        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_demo)

        initViewModel()
    }

    private fun initViewModel() {

        viewModel = ViewModelProvider(this).get(DataBindingViewModel::class.java)

        var userInfo = UserInfo()
        userInfo.name = "周杰伦"
        userInfo.nickName = "Jay"
        viewModel.userInfo.value = userInfo
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this

        dataBinding.listener = this

        viewModel.userInfo.observe(this) {


            LogUtils.d(TAG, "虎丘的带边的值${it.name}")
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            dataBinding.btnClick -> {

                Toast.makeText(this, viewModel.userInfo.value?.nickName, Toast.LENGTH_SHORT).show()

               viewModel.userInfo.value?.name = "haha "

                viewModel.userInfo.value = viewModel.userInfo.value!!

            }
        }
    }


}