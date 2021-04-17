package com.example.domain.database

import android.arch.persistence.room.TypeConverter
import com.example.entity.Coordinates
import com.google.gson.Gson

class CoordinatesTypeConverter{
    @TypeConverter
    fun fromJson(string:String)=Gson().fromJson(string,Coordinates::class.java)
    @TypeConverter
    fun toJson(coordnates:Coordinates)=Gson().toJson(coordnates)
}