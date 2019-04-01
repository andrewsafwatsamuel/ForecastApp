package com.example.domain

import com.example.entity.City
import com.example.entity.FavoriteCityId

val mockCityList = listOf(
    City(1L, "cairo", "Egypt", null),
    City(2L, "aswan", "Egypt", null),
    City(3L, "fayoum", "Egypt", null),
    City(4L, "asuit", "Egypt", null),
    City(5L, "giza", "Egypt", null),
    City(6L, "beniseuf", "Egypt", null),
    City(7L, "behera", "Egypt", null),
    City(8L, "Alexandria", "Egypt", null),
    City(9L, "qalubeya", "Egypt", null),
    City(10L, "sharkeya", "Egypt", null)

)

val mockFavouriteCityIds = listOf(
    FavoriteCityId(1L),
    FavoriteCityId(4L),
    FavoriteCityId(5L),
    FavoriteCityId(7L),
    FavoriteCityId(2L)
)