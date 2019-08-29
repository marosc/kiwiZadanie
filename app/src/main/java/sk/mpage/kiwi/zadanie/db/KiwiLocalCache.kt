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

package sk.mpage.kiwi.zadanie.db

import android.util.Log
import androidx.lifecycle.LiveData
import sk.mpage.kiwi.zadanie.model.Flight
import sk.mpage.kiwi.zadanie.model.FlightUsed
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class KiwiLocalCache(
        private val flightDao: FlightDao,
        private val ioExecutor: Executor
) {

    /**
     * Insert a list of flights in the database, on a background thread.
     */
    fun insert(flights: List<Flight>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val TODAY = format.format(Calendar.getInstance().time)
            //Log.d("KiwiLocalCache", "clearing and inserting ${flights.size} flights")
            flightDao.deleteAllFlights()
            val used = flightDao.flightsUsed(TODAY)

            //Log.d("FlightsUsed", "" + flightDao.yesterday(TODAY).toString())
            //Log.d("FlightsUsed", "all:" + flightDao.flightsUsedAll().toString())
            //Log.d("FlightsUsed", "used:" + used.toString())
            //Log.d("KiwiLocalCache", "flights:" + flights.toString())
            //Log.d("KiwiLocalCache", "total  ${flights.size} flights")

            val suggestions = flights.filterNot { used.contains(it.city_to) }.sortedBy { it.price }.subList(0, 5)

            //Log.d("KiwiLocalCache", "flights:" + suggestions.toString())
            Log.d("KiwiLocalCache", "total  ${suggestions.size} flights")

            flightDao.insert(suggestions)
            suggestions.forEach {
                flightDao.insertUsed(FlightUsed(it.city_to, TODAY))
            }
            insertFinished()
        }
    }

    /**
     * Request a LiveData<List<Flight>> from the Dao.
     */
    fun flightsSuggstions(): LiveData<List<Flight>> {
        return flightDao.flightsSuggestions()
    }

}
