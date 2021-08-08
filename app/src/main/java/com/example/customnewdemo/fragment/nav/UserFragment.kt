package com.example.customnewdemo.fragment.nav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.customnewdemo.databinding.FragmentUserBinding

class UserFragment : Fragment() {

    private lateinit var binding:FragmentUserBinding
    companion object{
        val TAG = "UserFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        Log.d(tag,"$TAG --> onCreateView")
        binding = FragmentUserBinding.inflate(inflater, container, false)

//        return super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }
}