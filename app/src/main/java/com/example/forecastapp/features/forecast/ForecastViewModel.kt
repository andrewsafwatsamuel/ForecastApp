package com.example.forecastapp.features.forecast

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.domain.CheckFavouriteUseCase
import com.example.domain.ImportForecastUseCase
import com.example.entity.ForecastsResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ForecastViewModel(
    val loadingSubject: BehaviorSubject<Boolean> = BehaviorSubject.create(),
    val responseSubject: BehaviorSubject<ForecastsResponse> = BehaviorSubject.create(),
    val disposables: CompositeDisposable = CompositeDisposable(),
    val favouriteString: MutableLiveData<String> = MutableLiveData(),
    private val importForecastUseCase: ImportForecastUseCase = ImportForecastUseCase(),
    val checkFavouriteUseCase: CheckFavouriteUseCase = CheckFavouriteUseCase(favouriteString)
) : ViewModel() {

    fun importForecasts(cityId: Long) {
        importForecastUseCase(cityId)
            .doOnSubscribe { loadingSubject.onNext(true) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { responseSubject.onNext(it) }
            .doAfterSuccess { loadingSubject.onNext(false) }
            .subscribe({}, Throwable::printStackTrace)
            .also { disposables.addAll() }
    }


    fun checkFavourites(cityId: Long) {
        Single.create<Unit> { checkFavouriteUseCase(cityId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .also { disposables.addAll() }
    }


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}