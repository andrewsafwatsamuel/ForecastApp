package com.example.domain.database
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.domain.application
import com.example.domain.applicationLiveData
import com.example.entity.City
import com.example.entity.FavoriteCityId

val citiesDatabase by lazy { initializeDatabase(applicationLiveData.application()) }

@Database(
    entities = [City::class, FavoriteCityId::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CoordinatesTypeConverter::class)
abstract class CitiesDatabase :RoomDatabase(){
    abstract fun citiesDao(): CitiesDao
    abstract fun favouriteCityIdDao(): FavouriteCitiesDao
}