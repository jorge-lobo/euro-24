package com.example.euro24.data.managers

import android.content.Context
import com.example.euro24.utils.json.JSONLoader
import com.example.euro24.utils.json.JSONParser
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class ManagerRepository(private val context: Context) {

    private val managers = mutableListOf<Manager>()

    init {
        loadManagersFromJson()
    }

    private fun loadManagersFromJson() {
        val jsonString = JSONLoader(context).loadJSONFromAsset("managers.json")

        val jsonObject = JSONParser<JsonObject>().deserialize(
            jsonString,
            object : TypeToken<JsonObject>() {}.type
        )

        val managersArray = jsonObject.getAsJsonArray("managers")
        val managersList: List<Manager> = JSONParser<List<Manager>>().deserialize(
            managersArray.toString(),
            object : TypeToken<List<Manager>>() {}.type
        )

        managers.addAll(managersList)
    }

    fun getManagerByTeamId(teamId: Int): Manager? {
        return managers.find { it.id == teamId }?.copy()
    }

}