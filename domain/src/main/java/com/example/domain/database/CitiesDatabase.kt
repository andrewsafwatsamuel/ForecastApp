package com.example.domain.database
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.domain.database.CitiesDao
import com.example.domain.database.FavouriteCitiesDao
import com.example.entity.City
import com.example.entity.FavoriteCityId

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