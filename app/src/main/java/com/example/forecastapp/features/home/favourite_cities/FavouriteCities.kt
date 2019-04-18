package com.example.forecastapp.features.home.favourite_cities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.forecastapp.R
import com.example.forecastapp.core.ContentViewId
import com.example.forecastapp.features.home.search.EXTRA_FAVOURITE_IDS
import com.example.forecastapp.subFeatures.cities_list.CitiesFragment
import com.example.forecastapp.subFeatures.cities_list.CitiesRecyclerViewAdapter


@ContentViewId(R.layout.activity_favourite_cities)
class FavouriteCities : AppCompatActivity() {

    private val vieModel by lazy {
        ViewModelProviders.of(this)[FavouriteCitiesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CitiesFragment()
            .also {loadFavouriteCities()}
            .also { drawCitiesFragment(it) }
            .also { it.publicRecyclerViewLiveData.observe(this, Observer {drawCitiesList(it!!)}) }
    }

    private fun loadFavouriteCities(){
        @Suppress("UNCHECKED_CAST")
        vieModel
            .importFavouriteCities(intent.getSerializableExtra(EXTRA_FAVOURITE_IDS) as List<Long>)
    }

    private fun drawCitiesFragment (fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.cities_frame_layout, fragment)
            .commit()
    }

    private fun drawCitiesList(recyclerView: RecyclerView) {
        recyclerView
            .also { it.layoutManager = LinearLayoutManager(this) }
            .also { it.adapter = CitiesRecyclerViewAdapter(this, vieModel.citiesResult) }
    }
}
