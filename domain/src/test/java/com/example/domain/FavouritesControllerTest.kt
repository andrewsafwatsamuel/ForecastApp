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

class FavouritesControllerTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `addToFavourites with cityId input then result will be true`() {
        val favouiteCities = ArrayList<FavoriteCityId>()
        val repositoryMock = mock<CitiesRepository> {
            on { importFavouriteCityIds() } doReturn  favouiteCities
        }
        val result = MutableLiveData<Boolean>()
        val cityID = 1L
        val addRemoveTestRepository = AddRemoveTestRepository(favouiteCities)

        with(
            FavouritesController(
                cityID,
                result,
                addRemoveTestRepository,
                CheckFavouriteUseCase(result, repositoryMock)
            )
        ) {
            addToFavourites()
        }
        assertTrue(result.value!!)
    }

    @Test
    fun `removeFromFavourites with cityId input then result will be false`() {
        val favouiteCities = ArrayList<FavoriteCityId>()
            .also {it.add(FavoriteCityId(1L))}
        val cityID = 1L
        val result = MutableLiveData<Boolean>()
        val addRemoveTestRepository = AddRemoveTestRepository(favouiteCities)
        val repositoryMock = mock<CitiesRepository> {
            on { importFavouriteCityIds() } doReturn favouiteCities
        }

        with(
            FavouritesController(
                cityID,
                result,
                addRemoveTestRepository,
                CheckFavouriteUseCase(result, repositoryMock)
            )
        ) {
            removeFromFavourites()
        }
        assertTrue(result.value==false)
    }


}

class AddRemoveTestRepository(private val favourites: ArrayList<FavoriteCityId>) : FakeCitiesRepository() {
    override fun insertToFavourites(favoriteCityId: FavoriteCityId) {
        favourites.add(favoriteCityId)
    }

    override fun removeFromFavourites(favoriteCityId: FavoriteCityId) {
        favourites.remove(favoriteCityId)
    }
}