package com.example.forecastapp.features.forecast

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.domain.ADD_TO_FAVOURITES
import com.example.entity.City
import com.example.entity.Forecast

import com.example.forecastapp.R
import com.example.forecastapp.core.ContentViewId
import com.example.forecastapp.subFeatures.EXTRA_CITY
import kotlinx.android.synthetic.main.activity_forecast.*

@ContentViewId(R.layout.activity_forecast)
class ForecastActivity : AppCompatActivity(), ForecastView {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ForecastViewModel::class.java)
    }

    private val presenter by lazy {
        ForecastPresenterImplementer(viewModel, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val city = intent.getSerializableExtra(EXTRA_CITY) as City

        presenter.also { it.initializeCity(city) }
            .also { favouriteButtonOnClick(it) }
    }

    private fun favouriteButtonOnClick(presenter: ForecastPresenter) {
        fovurites_switch_button.setOnClickListener { invokeFavouritesAction(presenter) }
    }

    fun invokeFavouritesAction(presenter: ForecastPresenter) {
        if (viewModel.favouriteString.value!!.equals(ADD_TO_FAVOURITES)) {
            presenter.addCityToFavoritesClicked()
        } else {
            presenter.removeCityFromFavorites()
        }
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

    override fun drawForecastList(forecasts: List<Forecast>) {
        MutableLiveData<List<Forecast>>()
            .also { it.value = forecasts }
            .let { ForecastRecyclerViewAdapter(this, it) }
            .also { forecasts_recycler_view.layoutManager = LinearLayoutManager(this) }
            .also { forecasts_recycler_view.adapter = it }
    }

    override fun switchFavoriteButtonState() {
        viewModel.favouriteString.observe(this, Observer { fovurites_switch_button.text = it })
    }

}
