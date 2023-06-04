package com.example.customnewdemo.jtpack

import android.app.Application

import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustomViewModel() : ViewModel() {


    var name  = MutableLiveData<String>().also {
        it.value = "周杰伦"
    }

}

class MyViewModel(context: Application) : AndroidViewModel(context) {


}