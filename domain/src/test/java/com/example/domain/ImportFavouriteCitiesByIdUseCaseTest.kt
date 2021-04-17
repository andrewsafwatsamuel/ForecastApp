package com.example.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.example.entity.City
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/*
* if retrieve in progress do not trigger action
* when all is ok start retrieving
*/
class ImportFavouriteCitiesByIdUseCaseTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    //when all is ok start retrieving
    @Test
    fun `invoke at successful conditions then return results`() {
        val isRetrieving = MutableLiveData<Boolean>()
        val result = MutableLiveData<List<City>>()
        val repositoryMock = mock<CitiesRepository> {
            on {
                importFavouriteCitiesById(ArrayList())
            } doReturn ArrayList<City>()
        }

        with(ImportFavouriteCitiesByIdUseCase(isRetrieving, result, repositoryMock)) {
            invoke(mockFavouriteCityIds.map { it.id })
        }

        assertTrue(result.value != null)
    }

    //if retrieve in progress do not trigger action
    @Test
    fun `invoke when retrieving in progress then do not return results`() {
        val isRetrieving = MutableLiveData<Boolean>()
        val result = MutableLiveData<List<City>>()
        val repositoryMock = mock<CitiesRepository> {
            on {
                importFavouriteCitiesById(ArrayList())
            } doReturn ArrayList<City>()
        }

        with(ImportFavouriteCitiesByIdUseCase(isRetrieving, result, repositoryMock)) {
            isRetrieving.postValue(true)
            invoke(mockFavouriteCityIds.map { it.id })
        }

        assertTrue(result.value == null)
    }

}