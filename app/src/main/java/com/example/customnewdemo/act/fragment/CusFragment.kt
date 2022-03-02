package com.example.customnewdemo.act.fragment

import android.view.View
import com.example.customnewdemo.app.base.BaseFragment
import com.example.customnewdemo.databinding.FragmentCusBinding

class CusFragment : BaseFragment() {

    private lateinit var binding: FragmentCusBinding

    override fun attachLayoutRes(): View {
        binding = FragmentCusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setUpListener() {

       binding.btnChange.setOnClickListener {
           binding.tvContent.text = "更改过后的测试 "
       }
    }

    override fun setUpView() {
    }

    override fun getNavigatorView(): Int = 0

    override fun getStatusBarView(): Int = 0
}