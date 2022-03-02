package com.example.customnewdemo.act

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.customnewdemo.R
import com.example.customnewdemo.adapter.CustStaggerAdapter
import com.example.customnewdemo.databinding.ActivityCustomRecyBinding
import com.example.customnewdemo.utils.GridSpaceItemDecoration
import com.example.customnewdemo.utils.dip2px

class CustomRecyActivity : AppCompatActivity() {


    companion object {
        public fun startSelf(activity: Activity) {
            activity.startActivity(Intent(activity, CustomRecyActivity::class.java))
        }
    }

    lateinit var binding: ActivityCustomRecyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomRecyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()
    }

    private val adapter : CustStaggerAdapter by lazy {
        val custStaggerAdapter = CustStaggerAdapter(this)

        custStaggerAdapter
    }
    private fun setUpAdapter() {

        val manager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.rvMain.layoutManager = manager
        binding.rvMain.addItemDecoration(GridSpaceItemDecoration(10.dip2px(),false).setNoShowSpace(0,0))
        binding.rvMain.adapter = adapter

        var strings = arrayListOf<String>()

        for (i in 0..30) {
            strings.add("测试 + $i")
        }

        adapter.refreshData(strings)

    }


}