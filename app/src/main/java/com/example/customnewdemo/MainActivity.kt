package com.example.customnewdemo

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.customnewdemo.act.*
import com.example.customnewdemo.act.viewmodel.MainViewModel
import com.example.customnewdemo.app.MyApplication
import com.example.customnewdemo.databinding.ActivityMainBinding
import com.example.customnewdemo.net.netState.NetStateChangeObserver
import com.example.customnewdemo.net.netState.NetStateChangeReceiver
import com.example.customnewdemo.net.netState.NetworkUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.functions.Consumer

class MainActivity : AppCompatActivity(), NetStateChangeObserver {

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        NetStateChangeReceiver.registerReceiver(this)
        requestPermission();
        viewModel.loadUserHead()
        binding.startActivity.setOnClickListener {
            MyApplication.startLogin();
        }


        viewModel.userhead.observe(this, Observer {
            binding.tvContent.text = it.description.toString()
        })

        binding.tvClick.setOnClickListener {


//            (viewModel.userhead.value as UserHeadBean).description = "storm"

            viewModel.updateValue()

        }

        setUpListener()

    }



    var rxPermissions: RxPermissions? =null
    /**
     * 权限申请
     */
    private fun requestPermission() {
        if (null == rxPermissions ){
            rxPermissions = RxPermissions(this)

        }
        rxPermissions?.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ?.subscribe(Consumer { granted->{
                if (granted) {

                } else {

                }
            } })


    }

    private fun setUpListener() {
        binding.tvPermission.setOnClickListener {
            PermissionInfoActivity.startSelf(this)
        }
        binding.tvCoroutines.setOnClickListener {
            CoroutinesActivity.startSelf(this)

        }

        binding.tvCamera2.setOnClickListener{
            Camera2Activity.startSelf(this)

        }
        binding.tvStartNav.setOnClickListener {
    //            NavActivity.startSelf(this as MainActivity)
            var intent = Intent(this, NavActivity::class.java)

            startActivity(intent)
        }

        binding.tvCamera.setOnClickListener {
            CameraActivity.startSelf(this)

        }
    }

    override fun onResume() {
        super.onResume()
        NetStateChangeReceiver.registerObserver(this)
    }

    override fun onPause() {
        super.onPause()
        NetStateChangeReceiver.unRegisterObserver(this)
    }

    override fun onDestroy() {
        NetStateChangeReceiver.unRegisterRecycler(this)

        super.onDestroy()
    }

    override fun onNetDisconnected() {
        Toast.makeText(this, "没有网络 ", Toast.LENGTH_SHORT).show()
        Log.d("storm", "没有网络")
    }

    override fun onNetConnected(netWorkType: NetworkUtils.NetworkType) {
        Toast.makeText(this, "网络切换  $netWorkType", Toast.LENGTH_SHORT).show()
        Log.d("storm", "网络切换  $netWorkType")
    }

}