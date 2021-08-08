package com.example.customnewdemo.fragment.nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.customnewdemo.databinding.FragmentNavMainBinding

class NavMainFragment : Fragment() {

    private lateinit var  binding: FragmentNavMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNavMainBinding.inflate(inflater,container,false)
//        return super.onCreateView(inflater, container, savedInstanceState)
        return  binding.root
    }
}