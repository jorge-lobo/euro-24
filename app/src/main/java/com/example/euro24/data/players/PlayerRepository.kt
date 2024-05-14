package com.example.euro24.data.players

import android.content.Context
import com.example.euro24.utils.json.JSONLoader
import com.example.euro24.utils.json.JSONParser
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class PlayerRepository(private val context: Context) {

    private val players = mutableListOf<Player>()

    init {
        loadPlayersFromJson()
    }

    private fun loadPlayersFromJson() {
        val jsonString = JSONLoader(context).loadJSONFromAsset("players_test.json")

        val jsonObject = JSONParser<JsonObject>().deserialize(
            jsonString,
            object : TypeToken<JsonObject>() {}.type
        )

        val playersArray = jsonObject.getAsJsonArray("players")
        val playersList: List<Player> = JSONParser<List<Player>>().deserialize(
            playersArray.toString(),
            object : TypeToken<List<Player>>() {}.type
        )

        players.addAll(playersList)
    }

    fun getPlayers(): List<Player> {
        return players.toList()
    }

    fun getPlayerById(id: Int): Player? {
        return players.find { it.id == id }?.copy()
    }

}