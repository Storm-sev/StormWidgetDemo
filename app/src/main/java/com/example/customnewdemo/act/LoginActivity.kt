package com.example.customnewdemo.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customnewdemo.R
import com.example.customnewdemo.app.login.LoginContext
import com.example.customnewdemo.databinding.ActivityLoginBinding

/**
 * 登录模块测试
 */
class LoginActivity : AppCompatActivity() {

    companion object {
        val TAG = "LoginActivity"

    }

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 获取登录状态
        val instance = LoginContext.getInstance()
        setUpListener()

    }

    private fun setUpListener() {
        binding.tvLogin.setOnClickListener {
            LoginContext.getInstance().login()

        }
        binding.tvLoginOut.setOnClickListener {
            LoginContext.getInstance().loginOut()
        }

        binding.tvPay.setOnClickListener {
            LoginContext.getInstance().pay()
        }

        binding.tvCollect.setOnClickListener {
            LoginContext.getInstance().collect()

        }


    }
}