package com.example.forecastapp.features.home.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.widget.Toast
import com.example.domain.*
import com.example.entity.City
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.io.Serializable


class HomeViewModel(
    private val disposables: CompositeDisposable = CompositeDisposable(),
    val showCityForecast: PublishSubject<Serializable> = PublishSubject.create(),
    val searchProgress: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val searchResults: MutableLiveData<List<City>> = ArrayList<City>().toMutableLiveData(),
    val searchCityByName: SearchCityByNameUseCase = SearchCityByNameUseCase(searchProgress, searchResults),
    val idsResult:MutableLiveData<List<Long>> = MutableLiveData(),
    val idsRetrieving:MutableLiveData<Boolean> =false.toMutableLiveData(),
    val importFavouriteIds: ImportFavouriteIdsUseCase =ImportFavouriteIdsUseCase(idsRetrieving,idsResult)
) : ViewModel() {


    fun onSearchButtonClicked(cityName: String?) {
        Observable
            .fromCallable { searchCityByName(cityName) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .also { disposables.addAll() }
    }

    private val emptyFavourites="no fovourite cities to show"

    fun onFavouritesButtonClicked(context: Context){
        Single.create<Unit> { importFavouriteIds() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Toast.makeText(context,emptyFavourites,Toast.LENGTH_SHORT).show() }
            .subscribe({},Throwable::printStackTrace)
            .also { disposables.addAll() }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}