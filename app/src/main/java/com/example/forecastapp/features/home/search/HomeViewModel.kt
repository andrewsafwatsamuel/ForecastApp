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
    val searchProgress: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val searchResults: MutableLiveData<List<City>> = ArrayList<City>().toMutableLiveData(),
    val searchCityByName: SearchCityByNameUseCase = SearchCityByNameUseCase(searchProgress, searchResults)
    ) : ViewModel() {

    fun onSearchButtonClicked(cityName: String?) {
        Observable
            .fromCallable { searchCityByName(cityName) }
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