package com.example.euro24.data.groups

import android.content.Context
import com.example.euro24.utils.json.JSONLoader
import com.example.euro24.utils.json.JSONParser
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class GroupRepository(private val context: Context) {

    private val groups = mutableListOf<Group>()

    init {
        loadGroupFromJson()
    }

    private fun loadGroupFromJson() {
        val jsonString = JSONLoader(context).loadJSONFromAsset("groups.json")

        val jsonObject = JSONParser<JsonObject>().deserialize(
            jsonString,
            object : TypeToken<JsonObject>() {}.type
        )

        val groupsArray = jsonObject.getAsJsonArray("groups")
        val groupsList: List<Group> = JSONParser<List<Group>>().deserialize(
            groupsArray.toString(),
            object : TypeToken<List<Group>>() {}.type
        )

        groups.addAll(groupsList)
    }

    fun getGroups(): List<Group> {
        return groups.toList()
    }
}