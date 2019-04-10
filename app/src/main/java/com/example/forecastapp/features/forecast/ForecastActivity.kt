package com.example.forecastapp.features.forecast

import android.arch.lifecycle.MutableLiveData
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.entity.Forecast
import com.example.forecastapp.R
import com.example.forecastapp.core.ContentViewId
import kotlinx.android.synthetic.main.activity_forecast.*


const val ADD_TO_FAVOURITES = "add to favourites"
const val REMOVE_FROM_FAVOURITES = "remove from favourites"

@ContentViewId(R.layout.activity_forecast)
class ForecastActivity : AppCompatActivity(), ForecastView {

    override fun drawCityTitle(cityTitle: String) {
        this.title = cityTitle
    }

    override fun startLoading() {
        forecasts_progress_bar.visibility = View.VISIBLE
        favourites_button_progress_bar.visibility = View.VISIBLE
    }

    override fun stopLoading() {
        forecasts_progress_bar.visibility = View.GONE
        favourites_button_progress_bar.visibility = View.GONE
    }

    override fun drawForcastList(forcasts: List<Forecast>) {
        ForecastsLiveData()
            .also { it.value = forcasts }
            .also { ForecastRecyclerViewAdapter(this, it) }
    }

    override fun drawAsFavoriteCity() {
        fovurites_switch_button.text = ADD_TO_FAVOURITES
    }

    override fun drawAsNotFavoriteCity() {
        fovurites_switch_button.text = REMOVE_FROM_FAVOURITES
    }
}