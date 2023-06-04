package com.example.customnewdemo.act.recycleview

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NavUtils
import androidx.core.app.TaskStackBuilder
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.customnewdemo.R
import com.example.customnewdemo.act.recycleview.adapter.CusDiffAdapter
import com.example.customnewdemo.act.recycleview.adapter.StudentDiffCallBack
import com.example.customnewdemo.common.holder.RvViewHolder
import com.example.customnewdemo.databinding.ActivityRecycleBinding

class RecycleActivity : AppCompatActivity() {

    companion object {
        public fun start(activity: Activity) {
            activity.startActivity(Intent(activity, RecycleActivity::class.java))
        }
    }

    lateinit var mBinding: ActivityRecycleBinding
    var adapter: CusDiffAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRecycleBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpAdapter();
        setUpListener()
    }

    private fun setUpListener() {
        mBinding.tvLoadTwo.setOnClickListener {
//            var intent = Intent();
//            var bundle = Bundle();
//            bundle.putBinder("bitmap",image)

            val upIntent = NavUtils.getParentActivityIntent(this)
            upIntent?.let {
                if (NavUtils.shouldUpRecreateTask(this, it)) { // 是否需要重建任务栈

                    TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(it)
                        .startActivities()
                }else{
                    NavUtils.navigateUpTo(this, it)

                }
            }


            var list = arrayListOf<StudentBean>()

            for (i in 0..20) {
                list.add(StudentBean(id = (i + 50), content = "测试数据 --> ${50 + i}"))
            }

            adapter!!.loadMoreData(list)

        }
        mBinding.tvLoadMore.setOnClickListener {
//            var list = arrayListOf<StudentBean>()
//
//            for (i in 0..20) {
//                list.add(StudentBean(id = (i+30), content = "测试数据 --> ${30 + i}"))
//            }
//
//            adapter!!.loadMoreData(list)


            val new = ArrayList<StudentBean>()
            new.addAll(adapter!!.newData)
            new.set(0,StudentBean(adapter!!.newData[0].id,"修改的东西 "))
            var result =
                DiffUtil.calculateDiff(StudentDiffCallBack(adapter!!.newData, new))
            adapter!!.newData.set(0, new[0])

            result.dispatchUpdatesTo(adapter!!)


        }
    }

    private fun setUpAdapter() {
        var pool = RecyclerView.RecycledViewPool();
        adapter = CusDiffAdapter(this)
        var viewHolder : RecyclerView.ViewHolder = adapter!!.onCreateViewHolder(mBinding.rvMain, 0)
        pool.putRecycledView(viewHolder)
        var manager = LinearLayoutManager(this)

        manager.orientation = LinearLayoutManager.VERTICAL
        manager.initialPrefetchItemCount = 4
        manager.recycleChildrenOnDetach = true
        with(mBinding.rvMain) {
            setRecycledViewPool(pool)
            layoutManager = manager
            adapter = this@RecycleActivity.adapter


        }

        var list = arrayListOf<StudentBean>()

        for (i in 0..10) {
            list.add(StudentBean(id = i, content = "测试数据 --> $i"))
        }

        adapter?.refreshData(list)

    }
}