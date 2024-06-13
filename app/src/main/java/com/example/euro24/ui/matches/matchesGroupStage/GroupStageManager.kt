package com.example.euro24.ui.matches.matchesGroupStage

import com.example.euro24.data.groups.GroupTieBreaker
import com.example.euro24.data.matches.Match
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.data.teams.Team
import com.example.euro24.data.teams.TeamRepository

class GroupStageManager(
    matchRepository: MatchRepository,
    private val teamRepository: TeamRepository
) {

    private val matches = matchRepository.getMatches()

    fun setupMatch(match: Match, team1: Team, team2: Team) {
        val victoryPoints = 3
        val drawPoints = 1

        val resultTeam1 = match.resultTeam1 ?: return
        val resultTeam2 = match.resultTeam2 ?: return

        team1.updateStats(resultTeam1, resultTeam2)
        team2.updateStats(resultTeam2, resultTeam1)

        when {
            resultTeam1 > resultTeam2 -> {
                team1.addWin(victoryPoints)
                team2.addLoss()
            }

            resultTeam1 < resultTeam2 -> {
                team1.addLoss()
                team2.addWin(victoryPoints)
            }

            else -> {
                team1.addDraw(drawPoints)
                team2.addDraw(drawPoints)
            }
        }

        teamRepository.updateTeam(team1)
        teamRepository.updateTeam(team2)
    }

    private fun Team.updateStats(goalsFor: Int, goalsAgainst: Int) {
        played = (played ?: 0) + 1
        this.goalsFor = (this.goalsFor ?: 0) + goalsFor
        this.goalsAgainst = (this.goalsAgainst ?: 0) + goalsAgainst
        goalDifference = (goalDifference ?: 0) + (goalsFor - goalsAgainst)
    }

    private fun Team.addWin(points: Int) {
        won = (won ?: 0) + 1
        this.points = (this.points ?: 0) + points
    }

    private fun Team.addLoss() {
        lost = (lost ?: 0) + 1
    }

    private fun Team.addDraw(points: Int) {
        drawn = (drawn ?: 0) + 1
        this.drawn = (this.drawn ?: 0) + points
    }

    fun calculateGroupTieBreakers(teamList: List<Team>): List<GroupTieBreaker> {
        return teamList.map { team ->
            GroupTieBreaker(
                team,
                getPointsInMatchesBetweenTeams(team, teamList),
                getGoalDifferenceInMatchesBetweenTeams(team, teamList),
                getGoalsScoredInMatchesBetweenTeams(team, teamList)
            )
        }
    }

    private fun getPointsInMatchesBetweenTeams(team: Team, teams: List<Team>): Int {
        return matches.filter { match ->
            (match.team1Id == team.id || match.team2Id == team.id) &&
                    teams.any { it.id == match.team1Id || it.id == match.team2Id }
        }.sumOf { match ->
            when {
                match.team1Id == team.id && (match.resultTeam1 ?: 0) > (match.resultTeam2 ?: 0) -> 3
                match.team2Id == team.id && (match.resultTeam2 ?: 0) > (match.resultTeam1 ?: 0) -> 3
                (match.resultTeam1 ?: 0) == (match.resultTeam2 ?: 0) -> 1
                else -> 0
            }.toInt()
        }
    }

    private fun getGoalDifferenceInMatchesBetweenTeams(team: Team, teams: List<Team>): Int {
        return matches.filter { match ->
            (match.team1Id == team.id || match.team2Id == team.id) &&
                    teams.any { it.id == match.team1Id || it.id == match.team2Id }
        }.sumOf { match ->
            when {
                match.team1Id == team.id -> (match.resultTeam1 ?: 0) - (match.resultTeam2 ?: 0)
                match.team2Id == team.id -> (match.resultTeam2 ?: 0) - (match.resultTeam1 ?: 0)
                else -> 0
            }
        }
    }

    private fun getGoalsScoredInMatchesBetweenTeams(team: Team, teams: List<Team>): Int {
        return matches.filter { match ->
            (match.team1Id == team.id || match.team2Id == team.id) &&
                    teams.any { it.id == match.team1Id || it.id == match.team2Id }
        }.sumOf { match ->
            when {
                match.team1Id == team.id -> match.resultTeam1 ?: 0
                match.team2Id == team.id -> match.resultTeam2 ?: 0
                else -> 0
            }
        }
    }

    fun sortTeamsByTieBreaker(groupTieBreaker: List<GroupTieBreaker>): List<Team> {
        return groupTieBreaker.sortedWith(
            compareByDescending<GroupTieBreaker> { it.team.points }
                .thenByDescending { it.pointsInMatchesBetweenTeams }
                .thenByDescending { it.goalDifferenceInMatchesBetweenTeams }
                .thenByDescending { it.goalsScoredInMatchesBetweenTeams }
                .thenByDescending { it.team.goalDifference }
                .thenByDescending { it.team.goalsFor }
        ).map { it.team }
    }

    fun getBestThirdPlacedTeams(groupRankings: Map<String, List<Team>>): List<Team> {
        val thirdPlacedTeams = groupRankings.values.mapNotNull { it.getOrNull(2) }
        return thirdPlacedTeams.sortedWith(compareByDescending<Team> { it.points }
            .thenByDescending { it.goalDifference }
            .thenByDescending { it.goalsFor })
            .take(4)
    }
}