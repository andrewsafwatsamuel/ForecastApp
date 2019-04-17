package com.example.forecastapp.features.forecast

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.entity.City
import com.example.entity.Forecast
import com.example.forecastapp.R
import com.example.forecastapp.core.ContentViewId
import com.example.forecastapp.subFeatures.cities_list.EXTRA_CITY
import kotlinx.android.synthetic.main.activity_forecast.*


const val ADD_TO_FAVOURITES = "add to favourites"
const val REMOVE_FROM_FAVOURITES = "remove from favourites"

@ContentViewId(R.layout.activity_forecast)
class ForecastActivity : AppCompatActivity(), ForecastView {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[ForecastViewModel::class.java]
    }

    private val presenter by lazy {
        ForecastPresenterImplementer(this, this, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter
            .also { initializeViews(it) }
    }

    override fun drawCityTitle(cityTitle: String) {
        this.title = cityTitle
    }

    override fun startLoading() {
        forecasts_progress_bar.visibility = View.VISIBLE
    }

    override fun stopLoading() {
        forecasts_progress_bar.visibility = View.GONE
    }

    override fun drawForcastList(forcasts: List<Forecast>) {
        ForecastsLiveData()
            .also { it.value = forcasts }
            .let { ForecastRecyclerViewAdapter(this, it) }
            .also { forecasts_recycler_view.layoutManager = LinearLayoutManager(this) }
            .also { forecasts_recycler_view.adapter = it }
    }

    override fun drawAsFavoriteCity() {
        fovurites_switch_button.text = ADD_TO_FAVOURITES
    }

    override fun drawAsNotFavoriteCity() {
        fovurites_switch_button.text = REMOVE_FROM_FAVOURITES
    }

    private fun initializeViews(presenter: ForecastPresenterImplementer) {
        fovurites_switch_button.setOnClickListener { presenter.onFavouriteButtonClick() }
        presenter.initializeCity(intent.getSerializableExtra(EXTRA_CITY) as City)
    }
}