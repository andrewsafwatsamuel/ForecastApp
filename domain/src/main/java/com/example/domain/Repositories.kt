package com.example.domain

import com.example.domain.database.citiesDatabase
import com.example.entity.City
import com.example.entity.FavoriteCityId

interface CitiesRepository {
    fun searchCityByName(name: String): List<City>
    fun importFavouriteCitiesById(ids: List<Long>): List<City>
    fun importFavouriteCityIds(): List<FavoriteCityId>
}

val citiesRepositoryImplementer by lazy { CitiesRepositoryImplementer() }

class CitiesRepositoryImplementer : CitiesRepository {
    override fun searchCityByName(name: String) = citiesDatabase.citiesDao().searchCityByName(name)
    override fun importFavouriteCitiesById(ids: List<Long>) = citiesDatabase.citiesDao().importCitiesByIds(ids)
    override fun importFavouriteCityIds() = citiesDatabase.favouriteCityIdDao().QueryAll()
}