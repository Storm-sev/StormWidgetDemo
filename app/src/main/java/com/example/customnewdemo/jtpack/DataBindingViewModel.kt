package com.example.customnewdemo.jtpack

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DataBindingViewModel : ViewModel() {

    var userInfo = MutableLiveData<UserInfo>()

}


class UserInfo : BaseObservable() {


    var name: String? = null

    var nickName: String? = null


}