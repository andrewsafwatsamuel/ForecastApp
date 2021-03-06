package com.example.forecastapp.subFeatures.cities_list

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.entity.City
import com.example.forecastapp.R
import java.io.Serializable

const val ACTION_OPEN_FORECAST_SCREEN = "com.example.forecastapp.subFeatures.cities_list.ACTION_OPEN_FORECAST_SCREEN"

const val EXTRA_CITY = "com.example.forecastapp.subFeatures.SELECTED_CITY"

typealias CitiesLiveData = MutableLiveData<List<City>>

class CitiesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val forecastButton by lazy { view.findViewById<Button>(R.id.show_forecast_button) }
    val cityTextView by lazy { view.findViewById<TextView>(R.id.city_item_textView) }
    fun bind(city: City) {
        cityTextView.text = city.name ?: ""
        forecastButton.setOnClickListener {
            Intent(ACTION_OPEN_FORECAST_SCREEN)
                .putExtra(EXTRA_CITY, city as Serializable)
                .also { view.context.sendBroadcast(it) }
        }
    }
}


class CitiesRecyclerViewAdapter(
    lifecycleOwner: LifecycleOwner,
    private val citiesLiveData: CitiesLiveData
) : RecyclerView.Adapter<CitiesViewHolder>() {




    init {
        citiesLiveData.observe(lifecycleOwner, Observer { notifyDataSetChanged() })
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CitiesViewHolder {
        return LayoutInflater.from(p0.context).inflate(
            R.layout.search_item, p0, false
        ).let { CitiesViewHolder(it) }
    }

    override fun getItemCount() = citiesLiveData.value!!.size

    override fun onBindViewHolder(viewHolder: CitiesViewHolder, position: Int) =
        viewHolder.bind(citiesLiveData.value!![position])

}
