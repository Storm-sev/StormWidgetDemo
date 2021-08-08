package com.example.customnewdemo.widget

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.lang.reflect.Field
import java.util.ArrayDeque


@Navigator.Name("fixFragment")
class FixFragmentNavigator(context: Context, fragmentManager: FragmentManager, containerId: Int) :
    FragmentNavigator(context, fragmentManager, containerId) {
    private val mContext: Context = context
    private val mFragmentManager: FragmentManager = fragmentManager
    private val mContainerId = containerId

    companion object {
        val TAG = "FixFragmentNavigator"
    }


    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (mFragmentManager.isStateSaved) {
            Log.i(
                FixFragmentNavigator.TAG, "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }

//        val frag = instantiateFragment(
//            mContext, mFragmentManager,
//            className, args
//        )

        var frag = mFragmentManager.findFragmentByTag(className)
        if (null == frag) {
            frag = instantiateFragment(mContext, mFragmentManager, className, args);

        }
        frag.arguments = args
        val ft = mFragmentManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

//        ft.replace(mContainerId, frag)


        val fragments = mFragmentManager.fragments
        for (fragment in fragments) {
            ft.hide(fragment)

        }
        if (!frag.isAdded) {
            ft.add(mContainerId, frag, className)
        }
        ft.show(frag)

        ft.setPrimaryNavigationFragment(frag)

        @IdRes val destId = destination.id
        // 反射获取

        var mBackStack: ArrayDeque<Int>? = null



        try {
            val filed: Field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
            filed.isAccessible = true
            mBackStack = (filed.get(this)) as ArrayDeque<Int>

        } catch (e: Exception) {
            e.printStackTrace()
            return null

        }
        val initialNavigation = mBackStack.isEmpty()
        // TODO Build first class singleTop behavior for fragments
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId)

        var isAdded: Boolean = false
        isAdded = if (initialNavigation) {
            true
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                mFragmentManager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            true
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key!!, value!!)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        // The commit succeeded, update our view of the world
        return if (isAdded) {
            mBackStack.add(destId)
            destination
        } else {
            null
        }
    }


    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }

}