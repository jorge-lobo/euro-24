package com.example.euro24.ui.matches.matchesGroupStage

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.data.groups.Group
import com.example.euro24.data.groups.GroupRepository
import com.example.euro24.data.groups.GroupTieBreaker
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.data.teams.Team
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.ui.common.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchesGroupStageViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val groupRepository: GroupRepository = GroupRepository(application)
    private val teamRepository: TeamRepository = TeamRepository(application)
    private val matchesRepository: MatchRepository = MatchRepository(application)

    private val groups = MutableLiveData<List<Group>>()
    val sortedGroups = MutableLiveData<List<Group>>()
    val textDropdownTitle = MutableLiveData<String?>()
    val teamsInSelectedGroup = MutableLiveData<List<Team>>()

    init {
        getGroups()
        loadInitialGroup()
    }

    private fun getGroups() {
        isLoading.value = true
        noDataAvailable.value = false

        viewModelScope.launch {
            try {
                val result = groupRepository.getGroups()
                handleGroupsResult(result)
            } catch (e: Exception) {
                onError(e.message)
            } finally {
                isLoading.value = false
                isRefreshing.value = false
            }
        }
    }

    private fun handleGroupsResult(result: List<Group>) {
        groups.value = result.sortedBy { it.id }
        sortedGroups.value = groups.value
        noDataAvailable.value = result.isEmpty()
    }

    private fun loadInitialGroup() {
        val groupA = sortedGroups.value?.find { it.id == 1 }
        groupA?.let {
            textDropdownTitle.value = it.groupName
            loadTeamsForGroup(groupA)
        }
    }

    fun loadTeamsForGroup(group: Group) {
        viewModelScope.launch {
            val teamList = group.teamsId.mapNotNull { teamRepository.getTeamById(it) }

            // Calculate tiebreaker criteria for each team
            val groupTieBreaker = teamList.map { team ->
                val pointsInMatchesBetweenTeams = getPointsInMatchesBetweenTeams(team, teamList)
                val goalDifferenceInMatchesBetweenTeams =
                    getGoalDifferenceInMatchesBetweenTeams(team, teamList)
                val goalsScoredInMatchesBetweenTeams =
                    getGoalsScoredInMatchesBetweenTeams(team, teamList)
                GroupTieBreaker(
                    team,
                    pointsInMatchesBetweenTeams,
                    goalDifferenceInMatchesBetweenTeams,
                    goalsScoredInMatchesBetweenTeams
                )
            }

            // Sort teams according to tiebreaker criteria
            val sortedTeams = groupTieBreaker.sortedWith(
                compareByDescending<GroupTieBreaker> { it.team.points }
                    .thenByDescending { it.pointsInMatchesBetweenTeams }
                    .thenByDescending { it.goalDifferenceInMatchesBetweenTeams }
                    .thenByDescending { it.goalsScoredInMatchesBetweenTeams }
                    .thenByDescending { it.team.goalDifference }
                    .thenByDescending { it.team.goalsFor }
            ).map { it.team }

            teamsInSelectedGroup.postValue(sortedTeams)
        }
    }

    private fun getPointsInMatchesBetweenTeams(team: Team, teams: List<Team>): Int {
        return matchesRepository.getMatches().filter { match ->
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
        return matchesRepository.getMatches().filter { match ->
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
        return matchesRepository.getMatches().filter { match ->
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

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}