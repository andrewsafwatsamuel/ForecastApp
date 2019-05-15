package com.example.forecastapp.features.home.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.forecastapp.R
import com.example.forecastapp.core.ContentViewId
import com.example.forecastapp.features.home.favourite_cities.FavouriteCities
import com.example.forecastapp.subFeatures.cities_list.CitiesFragmentHolder
import com.example.forecastapp.subFeatures.cities_list.CitiesRecyclerViewAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*

private var citiesHolder: CitiesFragmentHolder? = null

@ContentViewId(R.layout.activity_home)
class HomeActivity : AppCompatActivity() {

    private val homeHolder by lazy {
        ViewModelProviders.of(this)[HomeFragmentHolder::class.java]
    }
    private val citiesHolderInstance by lazy {
        ViewModelProviders.of(this)[CitiesFragmentHolder::class.java]
            .also { citiesHolder=it }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        citiesHolderInstance(R.id.cities_frame_layout,this)
        homeHolder(R.id.home_frame_layout, this)
    }
}

class HomeFragmentHolder(private val fragment: HomeFragment = HomeFragment()) : ViewModel() {
    var commit: Int = 0

    operator fun invoke(id: Int, activity: FragmentActivity) {
        activity.supportFragmentManager
            .takeUnless { commit == -1 }
            ?.let { it.beginTransaction() }
            ?.also { it.add(id, fragment) }
            ?.let { it.commit() }
            ?.also { commit = it }
    }

}

class HomeFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[HomeViewModel::class.java] }

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.search_button).setOnClickListener {
            viewModel.onSearchButtonClicked(search_edit_text.text.toString())
        }

        viewModel.searchProgress.observe(this, Observer {
            search_progress_bar.visibility = if (it!!) View.VISIBLE else View.GONE
        })

        citiesHolder!!.citiesRecyclerView.observe(this, Observer {
            drawCitiesList(it!!)
        })

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