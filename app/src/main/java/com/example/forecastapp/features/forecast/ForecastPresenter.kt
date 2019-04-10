package com.example.forecastapp.features.forecast

import android.arch.lifecycle.MutableLiveData
import com.example.entity.City
import com.example.entity.Forecast

typealias ForecastsLiveData=MutableLiveData<List<Forecast>>

class ForecastPresenterImplementer : ForecastPresenter {
    override fun initializeCity(city: City) {
    }

    override fun addCityToFavoritesClicked() {
   }

    override fun removeCityFromFavorites() {
   }
}