/*
 * Copyright (C) 2019 Maros Cavojsky, mpage.sk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sk.mpage.kiwi.zadanie.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search_repositories.*
import sk.mpage.kiwi.zadanie.Injection
import sk.mpage.kiwi.zadanie.R
import sk.mpage.kiwi.zadanie.model.Flight



class SearchFlightActivity : AppCompatActivity() {

    private lateinit var viewModel: FlightRepositoriesViewModel
    private val adapter = FlightAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_repositories)

        // get the view model
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
                .get(FlightRepositoriesViewModel::class.java)


        initAdapter()
        initSearch()
    }

    private fun initAdapter() {
        list.adapter = adapter
        list.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL ,false)
        showEmptyList(true)
        viewModel.flights.observe(this, Observer<List<Flight>> {
            Log.d("Activity", "list: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.submitList(it)

        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Log.d("Kiwi", it)
            Toast.makeText(this, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun initSearch() {
        viewModel.searchFlight()
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            list.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            list.visibility = View.VISIBLE
        }
    }


}
