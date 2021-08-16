package com.example.customnewdemo.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ActivitySimpleLazyBinding

class SimpleLazyActivity : AppCompatActivity() {


    lateinit var binding: ActivitySimpleLazyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimpleLazyBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}