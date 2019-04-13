package com.example.forecastapp.features.forecast

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.entity.Forecast
import com.example.entity.ForecastDetails
import com.example.forecastapp.R

class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val dateText by lazy { view.findViewById<TextView>(R.id.date_text_view) }
    val humedityText by lazy { view.findViewById<TextView>(R.id.humedity_value_text_view) }
    val pressureText by lazy { view.findViewById<TextView>(R.id.pressure_value_text_view) }
    val minTemperatureText by lazy { view.findViewById<TextView>(R.id.min_value_text_view) }
    val nowTemperatureText by lazy { view.findViewById<TextView>(R.id.now_value_text_view) }
    val maxTemperatureText by lazy { view.findViewById<TextView>(R.id.max_value_text_view) }

    fun bind(forecast: Forecast) {
        dateText.text = forecast.dateText

        val forecastDetails = forecast.details ?: ForecastDetails(
            null, null, null, null,
            null, null, null
        )

        humedityText.text = forecastDetails.humidity.toString()
        pressureText.text = forecastDetails.pressure.toString()
        minTemperatureText.text = forecastDetails.minimumTemperature.toString()
        nowTemperatureText.text = forecastDetails.temperature.toString()
        maxTemperatureText.text = forecastDetails.maximumTemperature.toString()
    }

}

class ForecastRecyclerViewAdapter(
    lifeCycleOwner: LifecycleOwner,
    private val forecastsLiveData: MutableLiveData<List<Forecast>>
) : RecyclerView.Adapter<ForecastViewHolder>() {

    init {
        forecastsLiveData.observe(lifeCycleOwner,
            Observer { notifyDataSetChanged() })
    }

    override fun onCreateViewHolder(view: ViewGroup, position: Int): ForecastViewHolder {
        return LayoutInflater.from(view.context)
            .inflate(R.layout.forecast_item, view, false)
            .let { ForecastViewHolder(it) }
    }

    override fun getItemCount(): Int {
        return forecastsLiveData.value!!.size
    }

    override fun onBindViewHolder(viewHolder: ForecastViewHolder, position: Int) {
        viewHolder.bind(forecastsLiveData.value!![position])
    }
}