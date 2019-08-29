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
import retrofit2.http.Headers

private const val TAG = "GeoService"


fun searchLocation(
        service: GeoService,
        onSuccess: (location: GeoSearchResponse?) -> Unit,
        onError: (error: String) -> Unit
) {

    Log.d(TAG, "search: ")
    service.searchLocation().enqueue(
            object : Callback<GeoSearchResponse> {
                override fun onFailure(call: Call<GeoSearchResponse>?, t: Throwable) {
                    t.printStackTrace()
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<GeoSearchResponse>?,
                        response: Response<GeoSearchResponse>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        onSuccess(response.body())
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}


interface GeoService {

    @Headers(
            "Accept: */*",
            "User-Agent: KiwiZadanie/1.0.0",
            "Cache-Control: no-cache"
    )
    @GET("json")
    fun searchLocation(): Call<GeoSearchResponse>

    companion object {
        private const val BASE_URL = "https://ipapi.co/"

        fun create(): GeoService {
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
                    .create(GeoService::class.java)
        }
    }

}