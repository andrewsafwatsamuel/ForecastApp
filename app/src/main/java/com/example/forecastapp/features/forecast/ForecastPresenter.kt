package com.example.forecastapp.features.forecast

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import com.example.domain.FavouritesController
import com.example.entity.City
import com.example.entity.Forecast
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

typealias ForecastsLiveData = MutableLiveData<List<Forecast>>

class ForecastPresenterImplementer(
    private val lifecycleOwner: LifecycleOwner,
    private val forecastView: ForecastView,
    private val forecastViewModel: ForecastViewModel,
    private val favouritesController: FavouritesController =
        FavouritesController(forecastViewModel.isFavourite)
) : ForecastPresenter {

    private var city: City? = null

    override fun initializeCity(city: City) {
        this.city = city
        forecastView.startLoading()
        forecastViewModel.checkFavourite(city.id)
        setFavouriteButtonText()
    }

    fun onFavouriteButtonClick() {
        forecastViewModel.isFavourite
            .also { if (it.value!!) removeCityFromFavorites() else addCityToFavoritesClicked() }
            .also {setFavouriteButtonText()}
    }

    private fun setFavouriteButtonText(){
        forecastViewModel.isFavourite.observe(lifecycleOwner, Observer {
            if (it!!)forecastView.drawAsNotFavoriteCity()
            else forecastView.drawAsFavoriteCity()
        })
    }

    override fun addCityToFavoritesClicked() {
        handleThreading { favouritesController.addToFavourites(city!!.id) }
    }

    override fun removeCityFromFavorites() {
        handleThreading { favouritesController.removeFromFavourites(city!!.id) }
    }

    private fun handleThreading(function: () -> Unit) {
        Single.create<Unit> { function() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, Throwable::printStackTrace)
            .also { forecastViewModel.disposables.addAll() }
    }
}