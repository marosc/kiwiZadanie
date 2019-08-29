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

package sk.mpage.kiwi.zadanie

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import sk.mpage.kiwi.zadanie.api.GeoService
import sk.mpage.kiwi.zadanie.api.KiwiService
import sk.mpage.kiwi.zadanie.data.KiwiRepository
import sk.mpage.kiwi.zadanie.db.FlightDatabase
import sk.mpage.kiwi.zadanie.db.KiwiLocalCache
import sk.mpage.kiwi.zadanie.ui.ViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [KiwiLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): KiwiLocalCache {
        val database = FlightDatabase.getInstance(context)
        return KiwiLocalCache(database.flightsDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [KiwiRepository] based on the [KiwiService] and a
     * [KiwiLocalCache]
     */
    private fun provideKiwiRepository(context: Context): KiwiRepository {
        return KiwiRepository(KiwiService.create(), GeoService.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideKiwiRepository(context))
    }
}
