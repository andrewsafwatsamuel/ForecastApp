package com.example.forecastapp.subFeatures.cities_list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.forecastapp.R
import com.example.forecastapp.features.forecast.ForecastActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.io.Serializable
import java.util.concurrent.TimeUnit

class CitiesFragment : Fragment() {

    private val recyclerViewLiveData = MutableLiveData<RecyclerView>()
    val publicRecyclerViewLiveData = recyclerViewLiveData as LiveData<RecyclerView>

    private val showCityForecast: PublishSubject<Serializable> = PublishSubject.create()

    private val resultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            showCityForecast.onNext(intent!!.getSerializableExtra(EXTRA_CITY))
        }
    }

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewLiveData.value = view.findViewById(R.id.cities_list)
        activity!!.registerReceiver(resultsReceiver, IntentFilter(ACTION_OPEN_FORECAST_SCREEN))
        showCityForecast.debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { startForecastScreen(it) }
            .also { disposables.addAll() }
    }

    private fun startForecastScreen(citySerializable: Serializable) {
        Intent(activity, ForecastActivity::class.java)
            .putExtra(EXTRA_CITY, citySerializable)
            .also { startActivity(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity!!.unregisterReceiver(resultsReceiver)
        disposables.dispose()
    }
}


