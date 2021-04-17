package com.example.forecastapp

import android.app.Application
import com.example.domain.Domain
import com.example.forecastapp.core.activityLifeCycleCallBacks

class ForecastApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(activityLifeCycleCallBacks)
        Domain(this)
    }
}