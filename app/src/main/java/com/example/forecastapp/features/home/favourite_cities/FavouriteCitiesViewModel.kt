package com.example.forecastapp.features.home.favourite_cities

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.domain.IdsLiveData
import com.example.domain.ImportFavouriteCitiesByIdUseCase
import com.example.domain.ImportFavouriteIdsUseCase
import com.example.domain.toMutableLiveData
import com.example.entity.City
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavouriteCitiesViewModel(
    private val disposables: CompositeDisposable = CompositeDisposable(),
    val isRetrieving: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val citiesResult: MutableLiveData<List<City>> = ArrayList<City>().toMutableLiveData(),
   private val importFavouriteCitiesByIdUseCase: ImportFavouriteCitiesByIdUseCase
    = ImportFavouriteCitiesByIdUseCase(isRetrieving, citiesResult),
    val idsResult:IdsLiveData = ArrayList<Long>().toMutableLiveData(),
    val idsRetrieving:MutableLiveData<Boolean> =false.toMutableLiveData(),
   private val importFavouriteIds: ImportFavouriteIdsUseCase
    = ImportFavouriteIdsUseCase(idsRetrieving,idsResult)

) : ViewModel() {

    fun retrieveFavouriteIds(){
        Single.fromCallable { importFavouriteIds() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},Throwable::printStackTrace)
            .also { disposables.addAll() }
    }

    fun importFavouriteCities(ids: List<Long>) {
       
        Single.fromCallable { importFavouriteCitiesByIdUseCase(ids) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, Throwable::printStackTrace)
            .also { disposables.addAll() }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}