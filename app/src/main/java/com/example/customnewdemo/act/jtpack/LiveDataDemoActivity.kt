package com.example.customnewdemo.act.jtpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.customnewdemo.databinding.ActivityLiveDataDemoAcivityBinding
import com.example.customnewdemo.jtpack.CustomViewModel
import com.example.customnewdemo.jtpack.UserInfo
import com.example.customnewdemo.utils.LogUtils

/**
 * jetpack LiveData.
 * @property mBinding ActivityLiveDataDemoAcivityBinding
 */
class LiveDataDemoActivity : AppCompatActivity() {


    companion object {
        val TAG = "LiveDataDemoAcivity"

    }

    private lateinit var mBinding: ActivityLiveDataDemoAcivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLiveDataDemoAcivityBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        setViewModel()
        setLiveData()
    }

    private lateinit var viewModel: CustomViewModel
    private fun setViewModel() {

        viewModel = ViewModelProvider(this).get(CustomViewModel::class.java)




    }


    private fun setLiveData() {

        mBinding.btnChangeData.setOnClickListener {

//            liveData.value = "周杰伦 JAY"
            viewModel.name.value = "周杰伦 JAY"
        }


        viewModel.name.observe(this) {

            mBinding.tvText.apply {
                text = it
            }
        }
    }


}