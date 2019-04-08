package com.example.domain

import com.example.domain.database.citiesDatabase
import com.example.entity.City
import com.example.entity.FavoriteCityId
import com.example.entity.ForecastsResponse
import io.reactivex.Single

interface CitiesRepository {
    fun searchCityByName(name: String): List<City>
    fun importFavouriteCitiesById(ids: List<Long>): List<City>
    fun importFavouriteCityIds(): List<FavoriteCityId>
    fun insertToFavourites(favoriteCityId: FavoriteCityId)
    fun removeFromFavourites(favoriteCityId: FavoriteCityId)
}

val citiesRepositoryImplementer by lazy { CitiesRepositoryImplementer() }

class CitiesRepositoryImplementer : CitiesRepository {
    override fun searchCityByName(name: String) = citiesDatabase.citiesDao().searchCityByName(name)
    override fun importFavouriteCitiesById(ids: List<Long>) =
        citiesDatabase.citiesDao().importCitiesByIds(ids)

    override fun importFavouriteCityIds() = citiesDatabase.favouriteCityIdDao().QueryAll()
    override fun insertToFavourites(favoriteCityId: FavoriteCityId) =
        citiesDatabase.favouriteCityIdDao().insert(favoriteCityId)

    override fun removeFromFavourites(favoriteCityId: FavoriteCityId) =
        citiesDatabase.favouriteCityIdDao().remove(favoriteCityId)
}

interface ForecastRepository {
    fun retrieveThreeDaysForecast(id: String): Single<ForecastsResponse>
}

val forecastRepositoryImplementer by lazy { ForecastRepositoryImplementer() }

class ForecastRepositoryImplementer : ForecastRepository {
    override fun retrieveThreeDaysForecast(id: String) = forecastApis.retrieveForecastForThreeDay(id)
}