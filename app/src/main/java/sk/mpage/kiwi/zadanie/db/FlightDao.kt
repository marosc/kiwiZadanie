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

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.mpage.kiwi.zadanie.model.Flight
import sk.mpage.kiwi.zadanie.model.FlightUsed

/**
 * Room data access object for accessing tables.
 */
@Dao
interface FlightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(flights: List<Flight>)

    @Query("DELETE FROM flights")
    fun deleteAllFlights()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsed(flight: FlightUsed)

    // Do a similar query as the search API:
    // Look for flights
    @Query("SELECT * FROM flights ORDER BY price ASC, city_to ASC LIMIT 5")
    fun flightsSuggestions(): LiveData<List<Flight>>

    @Query("SELECT city_to FROM flights_used WHERE used=date(:today,'-1 day') OR used=date(:today,'-2 days')")
    fun flightsUsed(today: String): List<String>

    @Query("SELECT * FROM flights_used")
    fun flightsUsedAll(): List<FlightUsed>

    @Query("select date(:today,'-2 days')")
    fun yesterday(today: String): String

}
