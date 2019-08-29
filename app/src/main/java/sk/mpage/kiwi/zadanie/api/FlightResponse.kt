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

package sk.mpage.kiwi.zadanie.api

import com.google.gson.annotations.SerializedName
import sk.mpage.kiwi.zadanie.model.Flight

/**
 * Immutable model class for a FlightResponse that holds all the information about a trip from API.
 * Objects of this type are received from the Kiwi API, therefore all the fields are annotated
 * with the serialized name.
 */

data class FlightResponse(
        @field:SerializedName("id") val id: String,
        @field:SerializedName("cityFrom") val city_from: String,
        @field:SerializedName("cityTo") val city_to: String,
        @field:SerializedName("dTimeUTC") val departure_time: Long,
        @field:SerializedName("aTimeUTC") val arrival_time: Long,
        @field:SerializedName("distance") val distance: Double,
        @field:SerializedName("fly_duration") val fly_duration: String,
        @field:SerializedName("price") val price: Double,
        @field:SerializedName("deep_link") val deep_link: String,
        @SerializedName("countryFrom") val country_from: CountryResponse,
        @SerializedName("countryTo") val country_to: CountryResponse,
        @field:SerializedName("mapIdfrom") val map_id_from: String,
        @field:SerializedName("mapIdto") val map_id_to: String,
        @field:SerializedName("airlines") val airlines: List<String>,
        @field:SerializedName("route") val route: List<Any>,
        @field:SerializedName("flyFrom") val flyFrom: String,
        @field:SerializedName("flyTo") val flyTo: String

) {
    fun getFlight(): Flight {
        return Flight(id,city_from,city_to,departure_time,arrival_time,distance,
                fly_duration,price,deep_link,country_from.code, country_to.code,
                map_id_from, map_id_to, airlines.joinToString(), route.size-1,
                flyFrom,flyTo)
    }
}
