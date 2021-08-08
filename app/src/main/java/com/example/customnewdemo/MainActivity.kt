package com.example.customnewdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.customnewdemo.act.CameraActivity
import com.example.customnewdemo.act.NavActivity
import com.example.customnewdemo.act.viewmodel.MainViewModel
import com.example.customnewdemo.app.MyApplication
import com.example.customnewdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

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

        binding.tvStartNav.setOnClickListener {
//            NavActivity.startSelf(this as MainActivity)
            var intent = Intent(this, NavActivity::class.java)

            startActivity(intent)
        }

        binding.tvCamera.setOnClickListener {
            CameraActivity.startSelf(this)

        }

    }


}