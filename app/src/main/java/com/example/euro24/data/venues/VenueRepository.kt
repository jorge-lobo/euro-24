package com.example.euro24.data.venues

import android.content.Context
import com.example.euro24.utils.json.JSONLoader
import com.example.euro24.utils.json.JSONParser
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class VenueRepository(private val context: Context) {

    private val venues = mutableListOf<Venue>()

    init {
        loadVenuesFromJson()
    }

    private fun loadVenuesFromJson() {
        val jsonString = JSONLoader(context).loadJSONFromAsset("venues.json")

        val jsonObject = JSONParser<JsonObject>().deserialize(
            jsonString,
            object : TypeToken<JsonObject>() {}.type
        )

        val venuesArray = jsonObject.getAsJsonArray("venues")
        val venuesList: List<Venue> = JSONParser<List<Venue>>().deserialize(
            venuesArray.toString(),
            object : TypeToken<List<Venue>>() {}.type
        )

        venues.addAll(venuesList)
    }

    fun getVenues(): List<Venue> {
        return venues.toList()
    }

    fun getVenueById(id: Int): Venue? {
        return venues.find { it.id == id }?.copy()
    }
}