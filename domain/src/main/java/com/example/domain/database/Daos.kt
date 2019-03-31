package com.example.domain.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.entity.City
import com.example.entity.FavoriteCityId

@Dao
interface CitiesDao {
    @Query("SELECT * FROM city WHERE City.name LIKE  '%' || :name || '%'")
    fun searchCityByName(name: String):List<City>

    @Query("SELECT * FROM city WHERE City.id IN (:ids)")
    fun retrieveCitiesByIds(ids: List<Long>):List<City>
}

@Dao
interface FavouriteCitiesDao {

    @Query("SELECT * FROM favoriteCityId")
    fun QueryAll():List<FavoriteCityId>

    @Insert
    fun insert(favouriteCityId: FavoriteCityId)

    @Delete
    fun remove(favouriteCityId: FavoriteCityId)
}