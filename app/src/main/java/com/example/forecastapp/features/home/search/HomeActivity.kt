package com.example.forecastapp.features.home.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.forecastapp.R
import com.example.forecastapp.core.ContentViewId
import com.example.forecastapp.features.home.favourite_cities.FavouriteCities
import com.example.forecastapp.subFeatures.cities_list.CitiesFragment
import com.example.forecastapp.subFeatures.cities_list.CitiesRecyclerViewAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*

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

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
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
            drawCitiesList(it!!)
        })

        view.findViewById<Button>(R.id.search_button).setOnClickListener {
            viewModel.onSearchButtonClicked(search_edit_text.text.toString())
        }

        view.findViewById<Button>(R.id.favourite_cities_button)
            .setOnClickListener { startFavouritesScreen() }
    }

    private fun drawCitiesList(recyclerView: RecyclerView) {
        recyclerView
            .also { it.layoutManager = LinearLayoutManager(context) }
            .also { it.adapter = CitiesRecyclerViewAdapter(this, viewModel.searchResults) }
    }

    private fun startFavouritesScreen() {
        Intent(context, FavouriteCities::class.java)
            .also { startActivity(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}