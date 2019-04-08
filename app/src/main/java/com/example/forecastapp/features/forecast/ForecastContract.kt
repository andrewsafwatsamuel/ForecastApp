package com.example.forecastapp.features.forecast

import android.arch.lifecycle.DefaultLifecycleObserver
import com.example.entity.City
import com.example.entity.Forecast

interface ForecastView {
    fun drawCityTitle(cityTitle: String)
    fun startLoading()
    fun stopLoading()
    fun drawForecastList(forecasts: List<Forecast>)
    fun switchFavoriteButtonState()
}

interface ForecastPresenter: DefaultLifecycleObserver {
    fun initializeCity(city: City)
    fun addCityToFavoritesClicked()
    fun removeCityFromFavorites()
}