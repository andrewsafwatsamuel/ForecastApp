package com.example.domain

import android.arch.lifecycle.MutableLiveData
import com.example.entity.City
import java.lang.Exception

class SearchCityByNameUseCase(
    private val isSearching: MutableLiveData<Boolean>,
    private val result: MutableLiveData<List<City>>,
    private val repository: CitiesRepository = citiesRepositoryImplementer
) {
    operator fun invoke(cityName: String?) {
        cityName
            ?.takeUnless { isSearching.value ?: false }
            ?.takeUnless { cityName.isBlank() }
            ?.also { isSearching.postValue(true) }
            ?.let { repository.searchCityByName(it) }
            ?.also { result.postValue(it) }
            ?.also { isSearching.postValue(false) }
    }
}

class ImportFavouriteCitiesByIdUseCase(
    private val isRetrieving: MutableLiveData<Boolean>,
    private val result: MutableLiveData<List<City>>,
    private val repository: CitiesRepository = citiesRepositoryImplementer
) {
    operator fun invoke(ids: List<Long>) {
        ids.takeUnless { isRetrieving.value ?: false }
            ?.also { isRetrieving.postValue(true) }
            ?.let { repository.importFavouriteCitiesById(it) }
            ?.also { result.postValue(it) }
            ?.also { isRetrieving.postValue(false) }
    }
}

class ImportFavouriteCityIdsUseCase(
    private val isRetrieving: MutableLiveData<Boolean>,
    private val result: MutableLiveData<List<Long>>,
    private val repository: CitiesRepository = citiesRepositoryImplementer
) {

    operator fun invoke() {
        repository.takeUnless { isRetrieving.value ?: false }
            ?.also { isRetrieving.postValue(true) }
            ?.let { it.importFavouriteCityIds() }
            ?.ifEmpty { throw Exception() }
            ?.map { it.id }
            ?.also { result.postValue(it) }
            ?.also { isRetrieving.postValue(false) }
    }
}

class ImportForecastUseCase(
    private val repository: ForecastRepository= forecastRepository
){
 operator fun invoke(cityId:Long)=repository.retrieveThreeDaysForecast(cityId.toString())
}