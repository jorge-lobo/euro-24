package com.example.euro24.data.teams

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type

class TeamRepository(private val context: Context) {

    private val teams = mutableListOf<Team>()
    private val gson = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .registerTypeAdapter(Team::class.java, TeamTypeAdapter())
        .create()

    init {
        loadTeamsFromInternalStorage()
    }

    private fun loadTeamsFromInternalStorage() {
        val jsonString = loadTeamsFileContent()
        if (jsonString.isNotEmpty()) {
            val teamsType: Type = object : TypeToken<List<Team>>() {}.type
            try {
                teams.addAll(gson.fromJson(jsonString, teamsType))
            } catch (e: Exception) {
                val teamsObject = JsonParser.parseString(jsonString).asJsonObject
                val teamsArray = teamsObject.getAsJsonArray("teams")
                teams.addAll(gson.fromJson(teamsArray, teamsType))
            }
        }
    }

    private fun loadTeamsFileContent(): String {
        val file = File(context.filesDir, "teams.json")
        return if (file.exists()) {
            file.readText()
        } else {
            ""
        }
    }

    fun getTeams(): List<Team> {
        return teams.toList()
    }

    fun getTeamById(id: Int): Team? {
        return teams.find { it.id == id }?.copy()
    }

    private fun saveTeamsToJson(teams: List<Team>) {
        val jsonString = gson.toJson(teams)
        val file = File(context.filesDir, "teams.json")
        file.writeText(jsonString)
    }

    fun updateTeam(team: Team) {
        val index = teams.indexOfFirst { it.id == team.id }
        if (index != -1) {
            teams[index] = team
            saveTeamsToJson(teams)
        }
    }
}