package com.example.customnewdemo.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.customnewdemo.MainActivity
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ActivitySplashBinding
import com.example.customnewdemo.utils.countDown
import com.example.customnewdemo.utils.startAct

class SplashActivity : AppCompatActivity() {

    lateinit var mDataBinding :  ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mDataBinding.root)


    }

    override fun onResume() {
        super.onResume()
        // 开启倒计时
//        countDownTimer.start()

        countDown(5,  {
            mDataBinding.tvCountDownTime.text = "开始记时间"
        }, {
            mDataBinding.tvCountDownTime.text = "倒计时${it}"
        },{
            startAct(MainActivity::class.java)
        })
    }

    val totalTime : Long = 5000;

    var countDownTimer = object : CountDownTimer(totalTime,1000){
        override fun onTick(millisUntilFinished: Long) {
            mDataBinding.tvCountDownTime.text =  "${millisUntilFinished/1000 }"
        }

        override fun onFinish() {
          startAct(MainActivity::class.java)
        }


    }

}