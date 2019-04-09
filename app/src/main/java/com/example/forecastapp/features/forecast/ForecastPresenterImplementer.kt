package com.example.forecastapp.features.forecast

import com.example.entity.City
import com.example.domain.addToFavourites
import com.example.domain.removeFromFavourites
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



class ForecastPresenterImplementer(
    val viewModel: ForecastViewModel,
    private val forecastView: ForecastView
) : ForecastPresenter {

    private var city: City? = null

    override fun initializeCity(city: City) {
        this.city = city
        city
            // .also { loadingLiveData.postValue(true) }
            .also { forecastView.drawCityTitle(it.name ?: "") }
            .also { forecastView.startLoading() }
            .also { viewModel.checkFavourites(it.id) }
            .also { viewModel.importForecasts(it.id) }
            .also { forecastView.stopLoading() }
            .also { forecastView.switchFavoriteButtonState() }

        viewModel.responseSubject
            .subscribe { forecastView.drawForecastList(it.forecasts!!) }
            ?.also { viewModel.disposables.addAll() }


        viewModel.loadingSubject
            .also { it.subscribe { loadingBehavior(it) } }
            .also { viewModel.disposables.addAll() }
    }


    private fun loadingBehavior(loading: Boolean) =
        if (loading) forecastView.startLoading() else forecastView.stopLoading()


    override fun addCityToFavoritesClicked() =
        addToFavouritesThread { addToFavourites(city!!.id, viewModel.favouriteString) }


    override fun removeCityFromFavorites() =
        addToFavouritesThread { removeFromFavourites(city!!.id, viewModel.favouriteString) }


    private fun addToFavouritesThread(threadMethod: () -> Unit) {
        Single.create<Unit> { threadMethod() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { forecastView.switchFavoriteButtonState() }
            .subscribe()
            .dispose()
    }
}