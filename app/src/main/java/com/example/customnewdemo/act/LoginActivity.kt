package com.example.customnewdemo.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ActivityLoginBinding

/**
 * 登录模块测试
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}