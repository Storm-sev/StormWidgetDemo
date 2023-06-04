package com.example.customnewdemo.act.jtpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope

import com.example.customnewdemo.act.room.RoomHelper
import com.example.customnewdemo.act.room.User
import com.example.customnewdemo.databinding.ActivityRoomDemoBinding
import com.example.customnewdemo.utils.LogUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jzvd.jzvideo.TAG

class RoomDemoActivity : AppCompatActivity() {

    companion object{

        val TAG =  "RoomDemoActivity"
    }


    private lateinit var mBinding: ActivityRoomDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRoomDemoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        showUser()
        addUser()
    }

    private fun showUser() {

        lifecycleScope.launch(Dispatchers.IO){

            val allUsers = RoomHelper.instance.allUsers()
            LogUtils.d(TAG, "${allUsers?.isEmpty()}")
            withContext(Dispatchers.Main){

                mBinding.tvContent.text = allUsers?.toList().toString()
            }
        }
    }

    private fun addUser() {
        mBinding.btnAdd.setOnClickListener {

            var name  = mBinding.etName.text.toString()
            var age = mBinding.etAge.text.toString()

            if (name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "未输入数据", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var user = User(userName = name, age = age.toInt())

            lifecycleScope.launch(Dispatchers.IO){

                RoomHelper.instance.addUser(user)

            }.start()


            showUser()


        }
    }
}