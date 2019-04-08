package com.example.forecastapp.features.forecast

import android.arch.lifecycle.MutableLiveData
import com.example.entity.City
import com.example.entity.ForecastsResponse
import com.example.domain.addToFavourites
import com.example.domain.removeFromFavourites
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.lang.NullPointerException


class ForecastPresenterImplementer(
    val viewModel: ForecastViewModel,
    private val forecastView: ForecastView,
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
) : ForecastPresenter {

    private var city: City? = null

    override fun initializeCity(city: City) {
        this.city = city
        city
            .also { loadingLiveData.postValue(true) }
            .also { forecastView.drawCityTitle(it.name ?: "") }
            .also { forecastView.startLoading() }
            .also { viewModel.checkFavourites(it.id) }
            .also { viewModel.importForecasts(it.id) }
            .also { forecastView.stopLoading() }
            .also { forecastView.switchFavoriteButtonState() }

        viewModel.responseSubject
            .takeIf { it.hasValue() }
            ?.also { subscribeServerResponse(it.value!!) }
            ?.subscribe()
            ?.also { viewModel.disposables.addAll() }


    }

    fun subscribeServerResponse(forecastsResponse: ForecastsResponse) {
        forecastsResponse
            .also { forecastView.drawForecastList(it.forecasts!!) }
            .also { loadingLiveData.postValue(false) }
    }

    private val disposable = CompositeDisposable()

    override fun addCityToFavoritesClicked() {
        city!!.id
            .also { addToFavouritesThread { addToFavourites(it) } }
            .also { viewModel.checkFavourites(it) }
            .also { forecastView.switchFavoriteButtonState() }
            .also { disposable.dispose() }
    }

    fun addToFavouritesThread(threadMethod: () -> Unit): Disposable {
        return Single.create<Unit> { threadMethod() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},Throwable::printStackTrace)
            .also { disposable.addAll() }
    }

    override fun removeCityFromFavorites() {
        city!!.id
            .also { addToFavouritesThread { removeFromFavourites(it) } }
            .also { viewModel.checkFavourites(it) }
            .also { forecastView.switchFavoriteButtonState() }
            .also { disposable.dispose() }
    }


}