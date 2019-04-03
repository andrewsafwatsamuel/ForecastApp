package com.example.domain

import com.example.entity.ForecastsResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val APP_ID_VALUE = "cc8bf0ef9fefd3794a362f69e9b0721d"
private const val OPEN_WEATHER_MAPS_URL = "http://api.openweathermap.org/"
private const val APP_ID_KEY = "appid"

val retrofitInstance by lazy {
    Retrofit.Builder().baseUrl(OPEN_WEATHER_MAPS_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

val forecastApis by lazy { retrofitInstance.create(ForecastApis::class.java) }

interface ForecastApis {
    @GET("data/2.5/forecast")
    fun retrieveForecastForThreeDay(
        @Query("id") id: String,
        @Query(APP_ID_KEY) appId: String = APP_ID_VALUE
    ): Single<ForecastsResponse>

}