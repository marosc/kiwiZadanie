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

package sk.mpage.kiwi.zadanie.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Immutable model class for a Flight that holds all the information about a trip.
 * Objects of this type are received from the Kiwi API, therefore all the fields are annotated
 * with the serialized name.
 * This class also defines the Room flights table, where the flight [id] is the primary key.
 */
@Entity(tableName = "flights", indices = [Index(value = ["city_to"], unique = true)])
data class Flight(
        @PrimaryKey val id: String,
        val city_from: String,
        val city_to: String,
        val departure_time: Long,
        val arrival_time: Long,
        val distance: Double,
        val fly_duration: String,
        val price: Double,
        val deep_link: String,
        val country_from: String,
        val country_to: String,
        val map_id_from: String,
        val map_id_to: String,
        val airlines: String,
        val stops: Int,
        val departure_aircode: String,
        val arrival_aircode: String

){

    fun getCountryToImage(): String{
        return "https://images.kiwi.com/photos/600x330/$map_id_to.jpg"
    }

    fun getFlagToImage(): String{
        return "https://www.countryflags.io/$country_to/shiny/64.png"
    }

    companion object {
        fun getAirlineToImage(airline: String): String {
            return "https://images.kiwi.com/airlines/64/$airline.png"
        }
    }
}
