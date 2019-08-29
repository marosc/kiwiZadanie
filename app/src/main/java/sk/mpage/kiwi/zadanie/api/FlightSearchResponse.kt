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

/**
 * Data class to hold flight responses from searchFlights API calls.
 */
data class FlightSearchResponse(
        @SerializedName("search_id") val search_id: String,
        @SerializedName("data") val items: List<FlightResponse> = emptyList()
)
