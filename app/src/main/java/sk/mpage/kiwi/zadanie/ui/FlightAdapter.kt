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

package sk.mpage.kiwi.zadanie.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sk.mpage.kiwi.zadanie.model.Flight

/**
 * Adapter for the list of repositories.
 */
class FlightAdapter : ListAdapter<Flight, androidx.recyclerview.widget.RecyclerView.ViewHolder>(FLIGHT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return FlightViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as FlightViewHolder).bind(repoItem)
        }
    }

    companion object {
        private val FLIGHT_COMPARATOR = object : DiffUtil.ItemCallback<Flight>() {
            override fun areItemsTheSame(oldItem: Flight, newItem: Flight): Boolean =
                    oldItem.id.compareTo(newItem.id) == 0

            override fun areContentsTheSame(oldItem: Flight, newItem: Flight): Boolean =
                    oldItem == newItem
        }
    }
}
