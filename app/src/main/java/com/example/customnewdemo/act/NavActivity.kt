package com.example.customnewdemo.act

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.customnewdemo.R
import com.example.customnewdemo.databinding.ActivityNavBinding
import com.example.customnewdemo.fragment.nav.HomeFragment
import com.example.customnewdemo.fragment.nav.ImgFragment
import com.example.customnewdemo.fragment.nav.NewsFragment
import com.example.customnewdemo.fragment.nav.UserFragment
import com.example.customnewdemo.widget.FixFragmentNavigator
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.storm.navdemo.widget.DotView
import com.storm.navdemo.widget.dip2px

class NavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 关联 navcontentview 和 底部bottom view
//        NavigationUI.setupWithNavController(binding.navMainBottom,navController

        var navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_host) as NavHostFragment
        navController = navHostFragment.navController

        val navigatorProvider = navController.navigatorProvider
        val fixFragmentNavigator =
            FixFragmentNavigator(this, navHostFragment.childFragmentManager, navHostFragment.id)

        navigatorProvider.addNavigator(fixFragmentNavigator)
        val navGraph = initNavGraph(navigatorProvider, fixFragmentNavigator)

        navController.graph = navGraph

//        NavigationUI.setupWithNavController(binding.navMainBottom, navController)


        setUpRedView()
        setUpListener()
    }

    private var navMenuView: BottomNavigationMenuView? = null
    private var redPoint: DotView? = null

    /**
     * 拓展 设置红点
     */
    private fun setUpRedView() {


        for (i in 0..binding.navMainBottom.childCount) {
            val child = binding.navMainBottom.getChildAt(i)
            if (child is BottomNavigationMenuView) {
                navMenuView = child
                break
            }
        }

        navMenuView?.let {

            var params = FrameLayout.LayoutParams(8.dip2px(), 0)
            params.gravity = Gravity.CENTER_HORIZONTAL
            params.leftMargin = 16.dip2px()
            params.topMargin = 4.dip2px()
            var itemView = it.getChildAt(1) as BottomNavigationItemView
            redPoint = DotView(this)
            itemView.addView(redPoint, params)
            redPoint?.show = true
        }

    }

    private fun setUpListener() {

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when (destination.id) {
                R.id.homeFragment -> {

                    Log.d(TAG, "homeFragment")

                    val checked = binding.navMainBottom.menu.getItem(0).isChecked;
                    if (!checked) {
                        binding.navMainBottom.menu.getItem(0).isChecked = true;
                    }


                }

                R.id.newsFragment -> {
                    Log.d(TAG, "newsfragment")

                    val checked = binding.navMainBottom.menu.getItem(1).isChecked;
                    if (!checked) {
                        binding.navMainBottom.menu.getItem(1).isChecked = true;
                    }

                }

                R.id.imgFragment -> {
                    Log.d(TAG, "imgFragment")
                    val checked = binding.navMainBottom.menu.getItem(2).isChecked;
                    if (!checked) {
                        binding.navMainBottom.menu.getItem(2).isChecked = true;
                    }

                }
                R.id.userFragment -> {

                    Log.d(TAG, "userragment")
                    val checked = binding.navMainBottom.menu.getItem(3).isChecked;
                    if (!checked) {
                        binding.navMainBottom.menu.getItem(3).isChecked = true;
                    }

                }
            }
        }

        binding.navMainBottom.setOnNavigationItemSelectedListener {
            navController.navigate(it.itemId)
            when (it.itemId) {
                R.id.homeFragment -> {


                }

                R.id.newsFragment -> {
                    redPoint?.show = false


                }
                R.id.imgFragment -> {


                }

                R.id.userFragment -> {


                }
            }

            true
        }


    }


    companion object {
        val TAG = "NavActivity"


        public fun startSelf(activity: Activity) {
            var intent = Intent(activity, NavActivity::class.java)
            activity.startActivity(intent)
        }
    }


    fun initNavGraph(
        navigatorProvider: NavigatorProvider,
        fragmentNavigator: FixFragmentNavigator
    ):
            NavGraph {
        val navGraph = NavGraph(NavGraphNavigator(navigatorProvider))
        val createDestination = fragmentNavigator.createDestination()
        with(createDestination) {
            id = R.id.homeFragment
            className = HomeFragment::class.java.canonicalName
            navGraph.addDestination(this)
        }


        val createDestination1 = fragmentNavigator.createDestination()
        with(createDestination1) {
            id = R.id.newsFragment
            className = NewsFragment::class.java.canonicalName
            navGraph.addDestination(this)
        }
        val createDestination2 = fragmentNavigator.createDestination()
        with(createDestination2) {
            id = R.id.imgFragment
            className = ImgFragment::class.java.canonicalName
            navGraph.addDestination(this)
        }
        val createDestination3 = fragmentNavigator.createDestination()
        with(createDestination3) {
            id = R.id.userFragment
            className = UserFragment::class.java.canonicalName
            navGraph.addDestination(this)
        }


        navGraph.startDestination = createDestination.id

        return navGraph
    }


    override fun onBackPressed() {

//        super.onBackPressed()
    }


}