package com.yqy.common.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import java.util.*


class ActivityManager : Application.ActivityLifecycleCallbacks {

    companion object {
        var taskTopActivity: FragmentActivity? = null
        val list: MutableList<FragmentActivity> =
            Collections.synchronizedList(LinkedList<FragmentActivity>())

        fun finishActivity(activity: FragmentActivity) {
            if (list.isEmpty()) return
            list.remove(activity)
            activity.finish()
        }

        fun clearAllActivity() {
            if (list.isEmpty()) return
            for (activity: FragmentActivity in list) {
                activity.finish()
            }
            list.clear()
        }
    }

    private fun addActivity(activity: FragmentActivity?) {
        if (activity != null && true) {
            list.add(activity)
        }
    }

    private fun removeActivity(activity: FragmentActivity?) {
        if (activity != null && true) {
            list.remove(activity)
        }
    }

    override fun onActivityPaused(activity: Activity?) {
        taskTopActivity = null
        DialogManager.dismissLoadingDialog()
    }

    override fun onActivityResumed(activity: Activity?) {
        if (activity != null && activity is FragmentActivity) {
            taskTopActivity = activity as FragmentActivity
        }
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
        if (activity is FragmentActivity)
            removeActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (activity is FragmentActivity)
            addActivity(activity)
    }

}