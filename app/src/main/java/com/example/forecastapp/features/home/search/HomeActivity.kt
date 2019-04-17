package com.example.forecastapp.features.home.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.forecastapp.R
import com.example.forecastapp.core.ContentViewId
import com.example.forecastapp.features.forecast.ForecastActivity
import com.example.forecastapp.subFeatures.cities_list.ACTION_OPEN_FORECAST_SCREEN
import com.example.forecastapp.subFeatures.cities_list.CitiesFragment
import com.example.forecastapp.subFeatures.cities_list.CitiesRecyclerViewAdapter
import com.example.forecastapp.subFeatures.cities_list.EXTRA_CITY
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.Serializable
import java.util.concurrent.TimeUnit

val citiesFragment = CitiesFragment()

@ContentViewId(R.layout.activity_home)
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .add(R.id.cities_frame_layout, citiesFragment)
            .commit()
    }

}

class HomeFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java) }

    private val resultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.showCityForecast.onNext(intent!!.getSerializableExtra(EXTRA_CITY))
        }
    }
    private val disposables = CompositeDisposable()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.searchProgress.observe(this, Observer {
            search_progress_bar.visibility = if (it!!) View.VISIBLE else View.GONE
        })

        citiesFragment.publicRecyclerViewLiveData.observe(this, Observer {
            addCitiesToList(it!!)
        })

        viewModel.showCityForecast.debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { startForecastScreen(it) }
            .also { disposables.addAll() }

        view.findViewById<Button>(R.id.search_button).setOnClickListener {
            val cityName = search_edit_text.text.toString()
            viewModel.onSearchButtonClicked(cityName)
        }
        activity!!.registerReceiver(resultsReceiver, IntentFilter(ACTION_OPEN_FORECAST_SCREEN))
    }

    private fun startForecastScreen(citySerializable: Serializable) {
        Intent(activity, ForecastActivity::class.java)
            .putExtra(EXTRA_CITY, citySerializable)
            .also { startActivity(it) }
    }

    private fun addCitiesToList(recyclerView: RecyclerView){
        recyclerView
            .also { it.layoutManager = LinearLayoutManager(context) }
            .also { it.adapter = CitiesRecyclerViewAdapter(this, viewModel.searchResults) }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity!!.unregisterReceiver(resultsReceiver)
        disposables.dispose()
    }
}