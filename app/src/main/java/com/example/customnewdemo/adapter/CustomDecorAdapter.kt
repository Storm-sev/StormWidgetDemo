package com.example.customnewdemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customnewdemo.R
import com.example.customnewdemo.common.holder.RvViewHolder

 class CustomDecorAdapter(private  var context: Context) :
    RecyclerView.Adapter<RvViewHolder>() {


    private var mData: ArrayList<String> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        return RvViewHolder.create(context, R.layout.item_decor, parent)

    }

    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {

        bindData(holder,position)
    }

     private fun bindData(holder: RvViewHolder, position: Int) {

         var tvContent  = holder.getView<TextView>(R.id.tv_content)

         tvContent.setText(mData.get(position))
     }

     override fun getItemCount(): Int  = mData.size

    @SuppressLint("NotifyDataSetChanged")
    public fun refreshData(list: List<String>?) {
        list?.let {
            mData.clear()
            mData.addAll(it)
            notifyDataSetChanged()
        }
    }

}
