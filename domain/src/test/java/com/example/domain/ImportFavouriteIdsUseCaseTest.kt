package com.example.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.example.entity.FavoriteCityId
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.Exception


/*
* if retrieving in progress then do not trigger action
* if favouriteIds list is empty throw exception
* when all is ok retrieve Ids list
*/
class ImportFavouriteIdsUseCaseTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    //when all is ok retrieve Ids list
    @Test
    fun `invoke with successful conditions return list of ids`() {
        val isRetrieving = MutableLiveData<Boolean>()
        val result = MutableLiveData<List<Long>>()
        val repositoryMock = mock<CitiesRepository> {
            on {
                importFavouriteCityIds()
            } doReturn mockFavouriteCityIds
        }

        with(ImportFavouriteIdsUseCase(isRetrieving, result, repositoryMock)) {
            invoke()
        }

        assertTrue(result.value != null)
    }

    //if favouriteIds list is empty throw exception
    @Test(expected = Exception::class)
    fun `invoke when ids list is empty then throw exception`() {
        val isRetrieving = MutableLiveData<Boolean>()
        val result = MutableLiveData<List<Long>>()
        val repositoryMock = mock<CitiesRepository> {
            on {
                importFavouriteCityIds()
            } doReturn ArrayList<FavoriteCityId>()
        }

        with(ImportFavouriteIdsUseCase(isRetrieving, result, repositoryMock)) {
            invoke()
        }
    }

    //if retrieving in progress then do not trigger action
    @Test
    fun `invoke while retrieving in progress then do not return results`() {
        val isRetrieving = MutableLiveData<Boolean>()
        val result = MutableLiveData<List<Long>>()
        val repositoryMock = mock<CitiesRepository> {
            on {
                importFavouriteCityIds()
            } doReturn mockFavouriteCityIds
        }

        with(ImportFavouriteIdsUseCase(isRetrieving, result, repositoryMock)) {
            isRetrieving.postValue(true)
            invoke()
        }

        assertTrue(result.value == null)
    }
}