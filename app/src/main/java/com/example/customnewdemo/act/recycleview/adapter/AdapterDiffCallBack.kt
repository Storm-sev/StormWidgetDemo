package com.example.customnewdemo.act.recycleview.adapter

import androidx.recyclerview.widget.DiffUtil

abstract  class AdapterDiffCallBack<T>(
     var oldData: MutableList<T>,
     var newData: MutableList<T>
) : DiffUtil.Callback() {


    override fun getOldListSize(): Int {
       return  oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }



}