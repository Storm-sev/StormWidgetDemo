package com.example.customnewdemo.act.recycleview.adapter

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.customnewdemo.R
import com.example.customnewdemo.act.recycleview.StudentBean
import com.example.customnewdemo.common.holder.RvViewHolder

class CusDiffAdapter : RecyclerView.Adapter<RvViewHolder> {

    lateinit var context: Context


    lateinit var newData: MutableList<StudentBean>

    constructor(context: Context) {
        this.context = context

        newData = ArrayList();
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {

        return RvViewHolder.create(context, R.layout.item_diff, parent)

    }


    override fun onBindViewHolder(holder: RvViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {

            var bundle = payloads[0] as Bundle
            bundle.keySet().forEach(){
                var content = bundle.getString(it)
                when(it){
                    "content" -> holder.getView<TextView>(R.id.tv_content).text = content
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return newData.size;
    }

    public fun refreshData(list: ArrayList<StudentBean>) {
        with(list) {

            var d = DiffUtil.calculateDiff(StudentDiffCallBack(newData, list))
            newData.addAll(list)
            d.dispatchUpdatesTo(this@CusDiffAdapter)
        }


    }


    public fun loadMoreData(list: ArrayList<StudentBean>) {
        with(list) {
            var data = ArrayList<StudentBean>()
            data.addAll(newData)
            data.addAll(list)
            var d = DiffUtil.calculateDiff(StudentDiffCallBack(newData, data))
            newData.addAll(list)
            d.dispatchUpdatesTo(this@CusDiffAdapter)
        }
    }

    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {

        val content = holder.getView<TextView>(R.id.tv_content)
        content.text = newData[position].content

    }


}