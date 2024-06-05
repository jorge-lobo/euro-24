package com.example.euro24.data.matches

import android.content.Context
import com.example.euro24.data.teams.Team
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.ui.matches.matchesGroupStage.GroupStageManager
import com.example.euro24.ui.matches.matchesKnockout.KnockoutStageManager
import com.example.euro24.utils.DateUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type

class MatchRepository(private val context: Context) {

    private val teamRepository = TeamRepository(context)
    private val matches = mutableListOf<Match>()
    private val gson = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .registerTypeAdapter(Match::class.java, MatchTypeAdapter())
        .create()
    private val knockoutStageManager = KnockoutStageManager(this)

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

    /*fun updateMatchResults(
        matchId: Int,
        team1Score: Int,
        team2Score: Int,
        team1ExtraTime: Int,
        team2ExtraTime: Int,
        team1Penalties: Int,
        team2Penalties: Int
    ) {
        val match = getMatchById(matchId)
        if (match != null) {
            if (DateUtils.currentDate.before(DateUtils.dateStartKnockout)) {
                match.resultTeam1 = team1Score
                match.resultTeam2 = team2Score
            } else {
                match.resultTeam1 = team1Score
                match.resultTeam2 = team2Score
                if (team1Score == team2Score) {
                    match.extraTimeTeam1 = team1ExtraTime
                    match.extraTimeTeam2 = team2ExtraTime
                    if (team1ExtraTime == team2ExtraTime) {
                        match.penaltiesTeam1 = team1Penalties
                        match.penaltiesTeam2 = team2Penalties
                    }
                }
            }
            saveMatch(match)
        }
    }*/

    val currentDate = DateUtils.formatter.parse("28/06/2024")
    fun updateMatchResults(
        matchId: Int,
        team1Score: Int,
        team2Score: Int,
        team1ExtraTime: Int,
        team2ExtraTime: Int,
        team1Penalties: Int,
        team2Penalties: Int
    ) {
        if (/*DateUtils.*/currentDate.before(DateUtils.dateStartKnockout)) {
            val match = getMatchById(matchId)
            match?.resultTeam1 = team1Score
            match?.resultTeam2 = team2Score
            saveMatch(match!!)
        } else {
            knockoutStageManager.updateKnockoutMatchResults(
                matchId,
                team1Score,
                team2Score,
                team1ExtraTime,
                team2ExtraTime,
                team1Penalties,
                team2Penalties
            )
        }
    }

    fun saveMatch(updatedMatch: Match) {
        val matchIndex = matches.indexOfFirst { it.id == updatedMatch.id }
        if (matchIndex != -1) {
            matches[matchIndex] = updatedMatch
        } else {
            matches.add(updatedMatch)
        }

        saveMatchesToJson(matches)
    }

    private fun saveMatchesToJson(matches: List<Match>) {
        val jsonString = gson.toJson(matches)
        val file = File(context.filesDir, "matches_test.json") // change to "matches.json"
        file.writeText(jsonString)
    }

    fun updateRoundOf16Matches(groupRankings: Map<String, List<Team>>) {
        val groupStageManager = GroupStageManager(this, teamRepository)
        val bestThirdPlacedTeams = groupStageManager.getBestThirdPlacedTeams(groupRankings)
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

        val thirdPlaceGroups = bestThirdPlacedTeams.mapNotNull { team ->
            when (team.id) {
                in 1..4 -> "A"
                in 5..8 -> "B"
                in 9..12 -> "C"
                in 13..16 -> "D"
                in 17..20 -> "E"
                in 21..24 -> "F"
                else -> null
            }
        }

        val roundOf16MatchIds = listOf(39, 40, 43, 41)

        val groupCombinations = thirdPlaceGroups.sorted().joinToString("")
        val placementMap = getPlacementMapForThirdPlaceTeams(groupCombinations)

        placementMap.forEachIndexed { index, group ->
            val teamId = bestThirdPlacedTeams.firstOrNull { it.id in getGroupRangeForGroup(group) }?.id
            val match = matches.find { it.id == roundOf16MatchIds[index] }
            match?.team2Id = teamId ?: 0
        }

        saveMatchesToJson(matches)
    }

    private fun getPlacementMapForThirdPlaceTeams(groupCombination: String): List<String> {
        val placements = mapOf(
            "ABCD" to listOf("A", "D", "B", "C"),
            "ABCE" to listOf("A", "E", "B", "C"),
            "ABCF" to listOf("A", "F", "B", "C"),
            "ABDE" to listOf("D", "E", "A", "B"),
            "ABDF" to listOf("D", "F", "A", "B"),
            "ABEF" to listOf("E", "F", "B", "A"),
            "ACDE" to listOf("E", "D", "C", "A"),
            "ACDF" to listOf("F", "D", "C", "A"),
            "ACEF" to listOf("E", "F", "C", "A"),
            "ADEF" to listOf("E", "F", "D", "A"),
            "BCDE" to listOf("E", "D", "B", "C"),
            "BCDF" to listOf("F", "D", "C", "B"),
            "BCEF" to listOf("F", "E", "C", "B"),
            "BDEF" to listOf("F", "E", "D", "B"),
            "CDEF" to listOf("F", "E", "D", "C")
        )
        return placements[groupCombination] ?: listOf()
    }

    private fun getGroupRangeForGroup(group: String): IntRange {
        return when (group) {
            "A" -> 1..4
            "B" -> 5..8
            "C" -> 9..12
            "D" -> 13..16
            "E" -> 17..20
            "F" -> 21..24
            else -> IntRange.EMPTY
        }
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