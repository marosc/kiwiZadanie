package sk.mpage.kiwi.zadanie.api

import java.text.SimpleDateFormat
import java.util.*

data class KiwiServiceParam(
        var lat_from: Double,
        var lon_from: Double,
        val days_limit: Int,
        val max_distance: Int
) {
    private val format = SimpleDateFormat("dd/MM/yyyy")

    fun fly_from(): String {
        return "$lat_from-$lon_from-${max_distance}km"
    }

    fun date_from(): String {
        return format.format(Calendar.getInstance().time)
    }

    fun date_to(): String {
        val date = Calendar.getInstance()
        date.add(Calendar.DATE, days_limit)
        return format.format(date.time)
    }
}