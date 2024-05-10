package com.example.euro24.data.teams

import android.content.Context
import com.example.euro24.utils.json.JSONLoader
import com.example.euro24.utils.json.JSONParser
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class TeamRepository(private val context: Context) {

    private val teams = mutableListOf<Team>()

    init {
        loadTeamsFromJson()
    }

    private fun loadTeamsFromJson() {
        val jsonString = JSONLoader(context).loadJSONFromAsset("teams.json")

        val jsonObject = JSONParser<JsonObject>().deserialize(
            jsonString,
            object : TypeToken<JsonObject>() {}.type
        )

        val teamsArray = jsonObject.getAsJsonArray("teams")
        val teamsList: List<Team> = JSONParser<List<Team>>().deserialize(
            teamsArray.toString(),
            object : TypeToken<List<Team>>() {}.type
        )

        teams.addAll(teamsList)
    }

    fun getTeams(): List<Team> {
        return teams.toList()
    }

    fun getTeamById(id: Int): Team? {
        return teams.find { it.id == id }?.copy()
    }

}