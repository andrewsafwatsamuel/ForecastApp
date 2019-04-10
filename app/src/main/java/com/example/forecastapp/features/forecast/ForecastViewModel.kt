package com.example.forecastapp.features.forecast

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.domain.CheckFavouriteUseCase
import com.example.domain.ImportForecastUseCase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ForecastViewModel(
    val disposables: CompositeDisposable = CompositeDisposable(),
    val isFavourite: MutableLiveData<Boolean> = MutableLiveData(),
    private val checkFavourites:CheckFavouriteUseCase=CheckFavouriteUseCase(isFavourite),
    private val importForecastUseCase: ImportForecastUseCase = ImportForecastUseCase()
) : ViewModel() {


    fun checkFavourite(cityId:Long) {
        Single.create<Unit> { checkFavourites(cityId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},Throwable::printStackTrace)
            .also { disposables.addAll() }
    }


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}