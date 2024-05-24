package com.example.euro24.data.matches

import android.content.Context
import com.example.euro24.data.teams.Team
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type

class MatchRepository(private val context: Context) {

    private val matches = mutableListOf<Match>()
    private val gson = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create()

    init {
        loadMatchesFromInternalStorage()
    }

    private fun loadMatchesFromInternalStorage() {
        val jsonString = loadMatchesFileContent()
        if (jsonString.isNotEmpty()) {
            val matchesType: Type = object : TypeToken<List<Match>>() {}.type
            try {
                matches.addAll(gson.fromJson(jsonString, matchesType))
            } catch (e: Exception) {
                val matchesObject = JsonParser.parseString(jsonString).asJsonObject
                val matchesArray = matchesObject.getAsJsonArray("matches")
                matches.addAll(gson.fromJson(matchesArray, matchesType))
            }
        }
    }

    private fun loadMatchesFileContent(): String {
        val file = File(context.filesDir, "matches_test.json") // change to "matches.json"
        return if (file.exists()) {
            file.readText()
        } else {
            ""
        }
    }

    fun getMatches(): List<Match> {
        return matches.toList()
    }

    fun getMatchById(id: Int): Match? {
        return matches.find { it.id == id }?.copy()
    }

    private fun saveMatchesToJson(matches: List<Match>) {
        val jsonString = gson.toJson(mapOf("matches" to matches))
        val file = File(context.filesDir, "matches_test.json") // change to "matches.json"
        file.writeText(jsonString)
    }

    fun updateRoundOf16Matches(groupRankings: Map<String, List<Team>>) {
        groupRankings.takeIf { it.isNotEmpty() }?.let { updateMatchesWithPlaceholders(it) }

        val roundOf16Matches = listOf(
            Pair("1A", "2C"), // Match 37
            Pair("1C", "3D/E/F"), // Match 38
            Pair("1B", "3A/D/E/F"), // Match 39
            Pair("1F", "2B"), // Match 40
            Pair("1E", "3A/B/C/D"), // Match 41
            Pair("1D", "2E"), // Match 42
            Pair("2A", "2B"), // Match 43
            Pair("1B", "3A/D/E/F")  // Match 44
        )

        roundOf16Matches.forEachIndexed { index, (team1Placeholder, team2Placeholder) ->
            val match = matches.find { it.id == 37 + index }
            match?.apply {
                team1Id = getTeamIdByPlaceholder(team1Placeholder, groupRankings) ?: team1Id
                team2Id = getTeamIdByPlaceholder(team2Placeholder, groupRankings) ?: team2Id
            }
        }

        saveMatchesToJson(matches)
    }

    private fun getTeamIdByPlaceholder(
        placeholder: String,
        groupRankings: Map<String, List<Team>>
    ): Int? {
        return if (placeholder.length == 2) {
            val rank = placeholder[0].toString().toInt()
            val group = placeholder[1].toString()

            groupRankings[group]?.getOrNull(rank - 1)?.id
        } else {
            val possibleGroups = placeholder.substring(1).split("/")

            possibleGroups.mapNotNull { groupRankings[it] }
                .flatten()
                .firstOrNull()
                ?.id
        }
    }

    private fun updateMatchesWithPlaceholders(groupRankings: Map<String, List<Team>>?) {
        if (groupRankings != null) {
            val matches = getMatches()

            matches.forEach { match ->
                val placeholder1 = match.team1
                val placeholder2 = match.team2

                val team1Id = placeholder1?.let { getTeamIdForPlaceholder(it, groupRankings) }
                val team2Id = placeholder2?.let { getTeamIdForPlaceholder(it, groupRankings) }

                if (match.id in 37..44) {
                    match.team1Id = team1Id ?: 0
                    match.team2Id = team2Id ?: 0
                }
            }

            saveMatchesToJson(matches)
        }
    }

    private fun getTeamIdForPlaceholder(
        placeholder: String,
        groupRankings: Map<String, List<Team>>
    ): Int? {
        return when (placeholder) {
            "1A" -> groupRankings["Group A"]?.getOrNull(0)?.id
            "2A" -> groupRankings["Group A"]?.getOrNull(1)?.id
            "1B" -> groupRankings["Group B"]?.getOrNull(0)?.id
            "2B" -> groupRankings["Group B"]?.getOrNull(1)?.id
            "1C" -> groupRankings["Group C"]?.getOrNull(0)?.id
            "2C" -> groupRankings["Group C"]?.getOrNull(1)?.id
            "1D" -> groupRankings["Group D"]?.getOrNull(0)?.id
            "2D" -> groupRankings["Group D"]?.getOrNull(1)?.id
            "1E" -> groupRankings["Group E"]?.getOrNull(0)?.id
            "2E" -> groupRankings["Group E"]?.getOrNull(1)?.id
            "1F" -> groupRankings["Group F"]?.getOrNull(0)?.id
            "2F" -> groupRankings["Group F"]?.getOrNull(1)?.id
            else -> {
                placeholder.split("/").let { parts ->
                    val rank = parts.first().toIntOrNull()
                    val group = parts.last()
                    groupRankings[group]?.getOrNull(rank?.minus(1) ?: -1)?.id
                }
            }
        }
    }
}