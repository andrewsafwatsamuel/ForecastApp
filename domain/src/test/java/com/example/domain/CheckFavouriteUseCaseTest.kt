package com.example.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.example.entity.FavoriteCityId
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.Assert.*


//check if in favourites
//if the city in favourite sit the value of mutableLiveData to true
//else sit the value of mutableLiveData to false
class CheckFavouriteUseCaseTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    //else sit the value of mutableLiveData to false
    @Test
    fun `invoke when city id outside favourite ids then favouriteCheck value is false`() {
        val repositoryMock = mock<CitiesRepository> {

            on { importFavouriteCityIds() } doReturn listOf(
                FavoriteCityId(1L), FavoriteCityId(2L), FavoriteCityId(3L)
            )
        }
        val favouriteState = MutableLiveData<Boolean>()
        val cityId = 5L

        with(CheckFavouriteUseCase(favouriteState,repositoryMock)) {
            invoke(cityId)
        }
        assertTrue(favouriteState.value==false)
    }

    //if the city in favourite sit the value of mutableLiveData to true
    @Test
    fun `invoke when city id inside favourite ids then favouriteCheck value is true`() {
        val repositoryMock = mock<CitiesRepository> {

            on { importFavouriteCityIds() } doReturn listOf(
                FavoriteCityId(1L), FavoriteCityId(2L), FavoriteCityId(3L)
            )
        }
        val favouriteState = MutableLiveData<Boolean>()
        val cityId = 2L

        with(CheckFavouriteUseCase(favouriteState,repositoryMock)) {
            invoke(cityId)
        }
        assertTrue(favouriteState.value==true)
    }
}