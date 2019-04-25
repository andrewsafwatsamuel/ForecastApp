package com.example.forecastapp.features.home.favourite_cities

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.forecastapp.R
import com.example.forecastapp.core.ContentViewId
import com.example.forecastapp.subFeatures.cities_list.CitiesFragment
import com.example.forecastapp.subFeatures.cities_list.CitiesRecyclerViewAdapter

@ContentViewId(R.layout.activity_favourite_cities)
class FavouriteCities : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[FavouriteCitiesViewModel::class.java]
    }

    private val citiesFragment by lazy { CitiesFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction().add(R.id.cities_frame_layout, citiesFragment)
            .commit()
    }

    override fun onPostResume() {
        super.onPostResume()
        loadCities()
    }

    private fun loadCities() {
        viewModel
            .also { it.retrieveFavouriteIds() }
            .also { observeIds(it) }
            .also { drawCitiesList() }
    }

    private fun observeIds(viewModel: FavouriteCitiesViewModel) {
        viewModel.idsResult.observe(this, Observer {
            viewModel.importFavouriteCities(it!!)
           /* println(it)*/
        })
    }

    private fun drawCitiesList() {
        citiesFragment
            .also { observeRecyclerView(it.publicRecyclerViewLiveData) }
    }

    private fun observeRecyclerView(recyclerViewLiveData: LiveData<RecyclerView>) {
        recyclerViewLiveData.observe(this, Observer { drawRecyclerView(it!!) })
    }

    private fun drawRecyclerView(recyclerView: RecyclerView) {
        recyclerView
            .also { it.layoutManager = LinearLayoutManager(this) }
            .also { it.adapter = CitiesRecyclerViewAdapter(this, viewModel.citiesResult) }
       /* viewModel.citiesResult.observe(this, Observer { println(it) })*/
    }
}