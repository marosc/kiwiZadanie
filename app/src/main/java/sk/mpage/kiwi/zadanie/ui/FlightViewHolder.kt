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

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.stfalcon.multiimageview.MultiImageView
import sk.mpage.kiwi.zadanie.R
import sk.mpage.kiwi.zadanie.model.Flight
import java.text.SimpleDateFormat


/**
 * View Holder for a [Flight] RecyclerView list item.
 */
class FlightViewHolder(view: View) : RecyclerView.ViewHolder(view), Target {
    private val cityText: TextView = view.findViewById(R.id.primary_text)
    private val subText: TextView = view.findViewById(R.id.sub_text)
    private val priceBtn: MaterialButton = view.findViewById(R.id.action_button_price)
    private val media_image: ImageView = view.findViewById(R.id.media_image)
    private val infoBox: ConstraintLayout = view.findViewById(R.id.moreinfo_box)
    private val airlines: MultiImageView = view.findViewById(R.id.airlines)

    private val depDateText: TextView = view.findViewById(R.id.departure_date)
    private val arrDateText: TextView = view.findViewById(R.id.arrival_date)
    private val depTimeText: TextView = view.findViewById(R.id.departure_time)
    private val arrTimeText: TextView = view.findViewById(R.id.arrival_time)
    private val depCodeText: TextView = view.findViewById(R.id.departure_aircode)
    private val arrCodeText: TextView = view.findViewById(R.id.arrival_aircode)
    private val buyBtn: Button = view.findViewById(R.id.detail_buy)
    private val stopsText: TextView = view.findViewById(R.id.stops)

    private var flight: Flight? = null

    init {
        priceBtn.setOnClickListener {
            flight?.deep_link?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }

        buyBtn.setOnClickListener {
            flight?.deep_link?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }

        view.setOnClickListener{
            if (infoBox.visibility==View.GONE){
                infoBox.visibility=View.VISIBLE
            }else{
                infoBox.visibility=View.GONE
            }
        }

    }


    fun bind(flight: Flight?) {
        flight?.let { showFlightData(flight) }
    }

    private fun showFlightData(flight: Flight) {
        this.flight = flight
        cityText.text = flight.city_to

        val dateFormat = SimpleDateFormat("EEE, dd.MM.")
        val timeFormat = SimpleDateFormat("HH:mm")

        val dateDep = java.util.Date(flight.departure_time * 1000)
        val dateArr = java.util.Date(flight.arrival_time * 1000)

        subText.text = "${dateFormat.format(dateDep)} / ${flight.fly_duration}"
        priceBtn.text = "${flight.price} €"

        Picasso.with(itemView.context)
                .load(flight.getCountryToImage())
                .into(media_image)

        airlines.clear()
        flight.airlines.split(",").forEach { airline ->
            Picasso.with(itemView.context).load(Flight.getAirlineToImage(airline)).into(this)
        }

        //info box
        depDateText.text=dateFormat.format(dateDep)
        arrDateText.text=dateFormat.format(dateArr)
        depTimeText.text=timeFormat.format(dateDep)
        arrTimeText.text=timeFormat.format(dateArr)
        if (flight.stops>0) {
            stopsText.text = "(${flight.stops})"
        }else{
            stopsText.text = ""
        }
        depCodeText.text = flight.departure_aircode
        arrCodeText.text = flight.arrival_aircode
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    }

    override fun onBitmapFailed(errorDrawable: Drawable?) {

    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        bitmap?.let { airlines.addImage(bitmap)}
    }

    companion object {
        fun create(parent: ViewGroup): FlightViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.flight_view_item, parent, false)
            return FlightViewHolder(view)
        }
    }
}
