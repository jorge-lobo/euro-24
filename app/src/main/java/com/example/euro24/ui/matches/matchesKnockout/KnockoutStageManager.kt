package com.example.euro24.ui.matches.matchesKnockout

import com.example.euro24.data.matches.Match
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.data.teams.Team

class KnockoutStageManager(private val matchRepository: MatchRepository) {

    fun updateKnockoutMatchResults(
        matchId: Int,
        team1Score: Int,
        team2Score: Int,
        team1ExtraTime: Int = 0,
        team2ExtraTime: Int = 0,
        team1Penalties: Int = 0,
        team2Penalties: Int = 0
    ) {
        val match = matchRepository.getMatchById(matchId)
        if (match != null) {
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

            matchRepository.saveMatch(match)

            val winnerId = determineWinner(match)
            if (winnerId != null) {
                match.id?.let { advanceWinner(it, winnerId) }
            }
        }
    }

    // Determina o vencedor de um jogo baseado nos resultados
    private fun determineWinner(match: Match): Int? {
        return when {
            match.resultTeam1!! > match.resultTeam2!! -> match.team1Id
            match.resultTeam2!! > match.resultTeam1!! -> match.team2Id
            match.extraTimeTeam1!! > match.extraTimeTeam2!! -> match.team1Id
            match.extraTimeTeam2!! > match.extraTimeTeam1!! -> match.team2Id
            match.penaltiesTeam1!! > match.penaltiesTeam2!! -> match.team1Id
            match.penaltiesTeam2!! > match.penaltiesTeam1!! -> match.team2Id
            else -> null
        }
    }

    private fun advanceWinner(matchId: Int, winnerId: Int) {
        val nextRoundMatchId = when (matchId) {
            // Round of 16
            37 -> 45
            38 -> 48
            39 -> 45
            40 -> 48
            41 -> 46
            42 -> 46
            43 -> 47
            44 -> 47
            // Quarter finals
            45 -> 49
            46 -> 49
            47 -> 50
            48 -> 50
            // Semi finals
            49 -> 51
            50 -> 51
            else -> null
        }

        nextRoundMatchId?.let { nextMatchId ->
            val nextMatch = matchRepository.getMatchById(nextMatchId)
            nextMatch?.apply {
                if (shouldBeTeam1(matchId, nextMatchId)) {
                    team1Id = winnerId
                } else {
                    team2Id = winnerId
                }
                matchRepository.saveMatch(this)
            }
        }
    }

    private fun shouldBeTeam1(matchId: Int, nextMatchId: Int): Boolean {
        return when (nextMatchId) {
            // Quarter finals
            45 -> matchId == 39
            46 -> matchId == 41
            47 -> matchId == 43
            48 -> matchId == 40
            // Semi finals
            49 -> matchId == 45
            50 -> matchId == 47
            // Final
            51 -> matchId == 49
            else -> false
        }
    }
}