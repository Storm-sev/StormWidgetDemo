package com.example.customnewdemo.fragment.nav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.customnewdemo.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private lateinit var binding:FragmentNewsBinding
    companion object{
        val TAG = "NewsFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"$TAG --> onCreateView")

        binding = FragmentNewsBinding.inflate(inflater,container,false)
//        return super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }
}