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

package sk.mpage.kiwi.zadanie.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import sk.mpage.kiwi.zadanie.api.*
import sk.mpage.kiwi.zadanie.db.KiwiLocalCache
import sk.mpage.kiwi.zadanie.model.FlightSearchResult

/**
 * Repository class that works with local and remote data sources.
 */
class KiwiRepository(
        private val service: KiwiService,
        private val geoService: GeoService,
        private val cache: KiwiLocalCache
) {

    // LiveData of network errors.
    private val networkErrors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Search flights
     */
    fun search(location: GeoSearchResponse?): FlightSearchResult {
        Log.d("KiwiRepository", "New query $location")

        if (location == null) {
            networkErrors.postValue("We can't find your location.")
        } else {
            requestAndSaveData(KiwiServiceParam(location.lat, location.lon, KiwiService.DAY_LIMIT, KiwiService.FROM_DISTANCE))
        }
        // Get data from the local cache
        val data = cache.flightsSuggstions()
        return FlightSearchResult(data, networkErrors)
    }


    fun requestLocation(onResult: (location: GeoSearchResponse?) -> Unit) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        searchLocation(geoService, { location ->
            onResult(location)
            isRequestInProgress = false
            if (location == null) {
                networkErrors.postValue("Location not found")
            }
        }, { error ->
            onResult(null)
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }


    private fun requestAndSaveData(params: KiwiServiceParam) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        searchLocation(geoService, { location ->
            if (location == null) {
                networkErrors.postValue("Location not found")
                isRequestInProgress = false
            } else {
                params.lat_from = location.lat

                searchFlights(service, params, { flights ->
                    cache.insert(flights) {
                        isRequestInProgress = false
                    }
                }, { error ->
                    networkErrors.postValue(error)
                    isRequestInProgress = false
                })
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })


    }
}
