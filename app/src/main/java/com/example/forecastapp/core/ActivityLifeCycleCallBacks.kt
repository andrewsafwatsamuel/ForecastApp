package com.example.forecastapp.core

import android.app.Activity
import android.app.Application
import android.os.Bundle

val activityLifeCycleCallBacks by lazy { ActivityLifeCycleCallBacks() }

class ActivityLifeCycleCallBacks:Application.ActivityLifecycleCallbacks{
    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val id=activity
            .javaClass.getAnnotation(ContentViewId::class.java)
            .id
        activity.setContentView(id)
    }

}