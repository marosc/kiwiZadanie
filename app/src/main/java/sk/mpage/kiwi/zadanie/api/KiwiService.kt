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

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sk.mpage.kiwi.zadanie.model.Flight

private const val TAG = "KiwiService"

/**
 * Search flights based on a query.
 * Trigger a request to the Kiwi API with the following params:
 * @param fly_from location of departure
 * @param date_from starting date for trip
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle the list of flights received
 * @param onError function that defines how to handle request failure
 */
fun searchFlights(
        service: KiwiService,
        params: KiwiServiceParam,
        onSuccess: (flights: List<Flight>) -> Unit,
        onError: (error: String) -> Unit
) {
    Log.d(TAG, "params: $params ${params.fly_from()}  ${params.date_from()}  ${params.date_to()}")


    service.searchFlights(
            params.fly_from(),
            params.date_from(),
            params.date_to()).enqueue(
            object : Callback<FlightSearchResponse> {
                override fun onFailure(call: Call<FlightSearchResponse>?, t: Throwable) {
                    t.printStackTrace()
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<FlightSearchResponse>?,
                        response: Response<FlightSearchResponse>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val data = response.body()?.items ?: emptyList()
                        onSuccess(data.map { it.getFlight() })
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

/**
 * Kiwi API communication setup via Retrofit.
 */
interface KiwiService {
    /**
     * Get flights ordered by quality.
     */
    @GET("flights?v=2&children=0&infants=0&curr=EUR&locale=en&sort=popularity&asc=0&vehicle_type=aircraft&limit=200&partner=picky&typeFlight=oneway&oneforcity=1")
    fun searchFlights(
            @Query("fly_from") fly_from: String,
            @Query("date_from") date_from: String,
            @Query("date_to") date_to: String
    ): Call<FlightSearchResponse>

    companion object {
        private const val BASE_URL = "https://api.skypicker.com/"
        const val DAY_LIMIT = 30
        const val FROM_DISTANCE = 200

        fun create(): KiwiService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(KiwiService::class.java)
        }
    }

}