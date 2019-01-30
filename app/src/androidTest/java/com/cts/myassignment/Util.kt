package com.cts.myassignment

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage

class Util {
    fun getActivityInstance(): Activity? {
        val activity =arrayOfNulls<Activity>(1)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val currentActivity: Activity?
            val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            if (resumedActivities.iterator().hasNext()) {
                currentActivity = resumedActivities.iterator().next() as Activity
                activity[0] = currentActivity
            }
        }

        return activity[0]
    }
}