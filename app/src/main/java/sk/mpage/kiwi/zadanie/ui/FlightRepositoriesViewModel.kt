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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import sk.mpage.kiwi.zadanie.api.GeoSearchResponse
import sk.mpage.kiwi.zadanie.data.KiwiRepository
import sk.mpage.kiwi.zadanie.model.Flight
import sk.mpage.kiwi.zadanie.model.FlightSearchResult

/**
 * ViewModel for the [SearchFlightActivity] screen.
 * The ViewModel works with the [KiwiRepository] to get the data.
 */
class FlightRepositoriesViewModel(private val repository: KiwiRepository) : ViewModel() {

    private val queryLiveData = MutableLiveData<GeoSearchResponse>()
    private val flightResult: LiveData<FlightSearchResult> = Transformations.map(queryLiveData) {
        repository.search(it)
    }

    val flights: LiveData<List<Flight>> = Transformations.switchMap(flightResult) { it?.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(flightResult) { it?.networkErrors }

    /**
     * Search flight
     */
    fun searchFlight() {
        repository.requestLocation({ location -> queryLiveData.postValue(location) })
    }


}
