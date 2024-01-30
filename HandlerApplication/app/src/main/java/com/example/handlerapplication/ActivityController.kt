package com.example.handlerapplication

import android.app.Activity

object ActivityController {
    private val activityList = ArrayList<Activity>()

    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityList.remove(activity)
    }

    fun finishLastActivity() {
        activityList.last().finish()
    }
}