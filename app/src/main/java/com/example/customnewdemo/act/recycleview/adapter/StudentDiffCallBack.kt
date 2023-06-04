package com.example.customnewdemo.act.recycleview.adapter

import android.os.Bundle
import com.example.customnewdemo.act.recycleview.StudentBean

class StudentDiffCallBack(old: MutableList<StudentBean>, new: MutableList<StudentBean>) :
    AdapterDiffCallBack<StudentBean>(old, new) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return oldData[oldItemPosition].id == newData[newItemPosition].id

    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].content == newData[newItemPosition].content


    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {

        var bundle = Bundle();
        val new = newData[newItemPosition]
        var old = oldData[oldItemPosition]

        if (old.content != new.content) {
            bundle.putString("content", new.content)
        }
        return if (bundle.size() == 0) null else bundle
    }
}