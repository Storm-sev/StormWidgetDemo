package com.example.customnewdemo.binderdemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anggrayudi.hiddenapi.InternalAccessor
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ActivityBinderServerBinding

class BinderServerActivity : AppCompatActivity() {

    private  lateinit var mBinding : ActivityBinderServerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBinderServerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        requestServer()
    }

    private fun requestServer() {




    }
}