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

import androidx.lifecycle.LiveData

/**
 * FlightSearchResult from a search, which contains LiveData<List<Flight>> holding query data,
 * and a LiveData<String> of network error state.
 */
data class FlightSearchResult(
        val data: LiveData<List<Flight>>,
        val networkErrors: LiveData<String>
)
