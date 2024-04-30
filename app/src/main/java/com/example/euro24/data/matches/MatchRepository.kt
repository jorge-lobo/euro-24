package com.example.euro24.data.matches

import android.content.Context
import com.example.euro24.utils.json.JSONLoader
import com.example.euro24.utils.json.JSONParser
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class MatchRepository(private val context: Context) {

    private val matches = mutableListOf<Match>()

    init {
        loadMatchesFromJson()
    }

    private fun loadMatchesFromJson() {
        val jsonString = JSONLoader(context).loadJSONFromAsset("matches_test.json")

        val jsonObject = JSONParser<JsonObject>().deserialize(
            jsonString,
            object : TypeToken<JsonObject>() {}.type
        )

        val matchesArray = jsonObject.getAsJsonArray("matches")
        val matchesList: List<Match> = JSONParser<List<Match>>().deserialize(
            matchesArray.toString(),
            object : TypeToken<List<Match>>() {}.type
        )

        matches.addAll(matchesList)
    }

    fun getMatches(): List<Match> {
        return matches.toList()
    }

    fun getMatchById(id: Int): Match? {
        return matches.find { it.id == id }?.copy()
    }

    fun getMatchByDate(date: String): Match? {
        val foundMatch = matches.find { it.date == date }
        return foundMatch?.copy()
    }

    fun getMatchByVenue(venueId: Int): Match? {
        val foundMatch = matches.find { it.venueId == venueId }
        return foundMatch?.copy()
    }

    fun getMatchByStage(phase: String): Match? {
        val foundMatch = matches.find { it.phase == phase }
        return foundMatch?.copy()
    }

    fun getMatchByTeam(team1Id: Int, team2Id: Int): Match? {
        val foundMatch = matches.find { it.team1Id == team1Id || it.team2Id == team2Id }
        return foundMatch?.copy()
    }
}