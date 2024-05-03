package com.example.euro24.data.pastFinals

import android.content.Context
import com.example.euro24.utils.json.JSONLoader
import com.example.euro24.utils.json.JSONParser
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class PastFinalRepository(private val context: Context) {

    private val pastFinals = mutableListOf<PastFinal>()

    init {
        loadPastFinalsFromJson()
    }

    private fun loadPastFinalsFromJson() {
        val jsonString = JSONLoader(context).loadJSONFromAsset("pastFinals.json")

        val jsonObject = JSONParser<JsonObject>().deserialize(
            jsonString,
            object : TypeToken<JsonObject>() {}.type
        )

        val pastFinalsArray = jsonObject.getAsJsonArray("pastFinals")
        val pastFinalsList: List<PastFinal> = JSONParser<List<PastFinal>>().deserialize(
            pastFinalsArray.toString(),
            object : TypeToken<List<PastFinal>>() {}.type
        )

        pastFinals.addAll(pastFinalsList)
    }

    fun getPastFinals(): List<PastFinal> {
        return pastFinals.toList()
    }

    fun getPastFinalById(id: Int): PastFinal? {
        return pastFinals.find { it.id == id }?.copy()
    }
}