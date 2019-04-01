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
* if searching in progress do not trigger action
* if cityName is blank do not trigger action
* if all is ok then search*/
class SearchCityByNameUseCaseTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    //if all is ok then search
    @Test
    fun `invoke on successful response then result is not null`() {
        val repositoryMock = mock<CitiesRepository> {
            on { searchCityByName(String()) } doReturn mockCityList
        }
        val isSearching = MutableLiveData<Boolean>()
        val result = MutableLiveData<List<City>>()
        with(SearchCityByNameUseCase(isSearching, result, repositoryMock)) {
            invoke("cai")
        }

        assertTrue(result.value != null)

    }

    //if cityName is blank do not trigger action
    @Test
    fun `invoke when cityName is blank then result value is null)`() {

        val repositoryMock = mock<CitiesRepository> {
            on { searchCityByName(String()) } doReturn mockCityList
        }
        val isSearching = MutableLiveData<Boolean>()
        val result = MutableLiveData<List<City>>()

        with(SearchCityByNameUseCase(isSearching, result, repositoryMock)) {
            invoke(String())
        }
        assertTrue(result.value == null)
    }

    //if searching in progress do not trigger action
    @Test
    fun `invoke when search in progress then result value is null)`() {

        val repositoryMock = mock<CitiesRepository> {
            on { searchCityByName(String()) } doReturn mockCityList
        }
        val isSearching = MutableLiveData<Boolean>()
        val result = MutableLiveData<List<City>>()

        with(SearchCityByNameUseCase(isSearching, result, repositoryMock)) {
            isSearching.postValue(true)
            invoke("cai")
        }

        assertTrue(result.value == null)
    }
}

