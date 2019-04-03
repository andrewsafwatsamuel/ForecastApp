package com.example.forecastapp.features.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import com.example.forecastapp.subFeatures.CitiesRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_home.*

@ContentViewId(R.layout.activity_home)
class HomeActivity : AppCompatActivity()

class HomeFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java) }


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
        view.findViewById<RecyclerView>(R.id.results_recycler_view)
            .also { it.layoutManager = LinearLayoutManager(context) }
            ?.also { it.adapter = CitiesRecyclerViewAdapter(this, viewModel.searchResults) }



        view.findViewById<Button>(R.id.search_button).setOnClickListener {
            val cityName = search_edit_text.text.toString()
            viewModel.onSearchButtonClicked(cityName)
        }
    }
}