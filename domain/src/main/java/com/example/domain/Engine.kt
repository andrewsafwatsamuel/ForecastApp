package com.example.domain

import android.app.Application
import android.arch.lifecycle.MutableLiveData

internal val applicationLiveData = MutableLiveData<Application>()

internal fun MutableLiveData<Application>.application() = value!!

object Domain {
    operator fun invoke(application: Application) {
        applicationLiveData.value = application
    }
}

fun <T> T.toMutableLiveData() = MutableLiveData<T>().also { it.value = this }
