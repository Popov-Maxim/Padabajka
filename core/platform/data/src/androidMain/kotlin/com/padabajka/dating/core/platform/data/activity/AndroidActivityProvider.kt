package com.padabajka.dating.core.platform.data.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle

class AndroidActivityProvider(application: Application) : Application.ActivityLifecycleCallbacks {
    private var resumedActivity: Activity? = null

    // Read on the main thread immediately before launching UI.
    val currentActivity: Activity?
        get() = resumedActivity?.takeUnless { it.isFinishing || it.isDestroyed }

    init {
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityResumed(activity: Activity) {
        resumedActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        clearActivity(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        clearActivity(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    private fun clearActivity(activity: Activity) {
        if (resumedActivity === activity) {
            resumedActivity = null
        }
    }
}
