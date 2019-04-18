package com.example.forecastapp.features.home.favourite_cities

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.domain.ImportFavouriteCitiesByIdUseCase
import com.example.domain.toMutableLiveData
import com.example.entity.City
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavouriteCitiesViewModel(
    private val diposables: CompositeDisposable = CompositeDisposable(),
    val isRetrieving: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val citiesResult: MutableLiveData<List<City>> = ArrayList<City>().toMutableLiveData(),
    val importFavouriteCitiesByIdUseCase: ImportFavouriteCitiesByIdUseCase
    = ImportFavouriteCitiesByIdUseCase(isRetrieving, citiesResult)
) : ViewModel() {

    fun importFavouriteCities(ids: List<Long>) {
        Single.create<Unit> { importFavouriteCitiesByIdUseCase(ids) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, Throwable::printStackTrace)
            .also { diposables.addAll() }
    }

    override fun onCleared() {
        super.onCleared()
        diposables.dispose()
    }
}