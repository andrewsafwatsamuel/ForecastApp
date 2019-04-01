package com.example.forecastapp

import android.app.Application
import com.example.domain.Domain

class ForecastApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Domain(this)
    }
}