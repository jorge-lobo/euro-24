package com.example.euro24.ui.matches.matchesGroupStage

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.data.groups.Group
import com.example.euro24.data.groups.GroupRepository
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.data.teams.Team
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.ui.matches.MatchNarrowCardBindingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchesGroupStageViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val groupRepository: GroupRepository = GroupRepository(application)
    private val teamRepository: TeamRepository = TeamRepository(application)
    private val matchRepository: MatchRepository = MatchRepository(application)
    private val groupStageManager: GroupStageManager =
        GroupStageManager(matchRepository, teamRepository)
    private val matches = matchRepository.getMatches()

    val sortedGroups = MutableLiveData<List<Group>>()
    val textDropdownTitle = MutableLiveData<String?>()
    val teamsInSelectedGroup = MutableLiveData<List<Team>>()
    val matchesInSelectedGroup = MutableLiveData<List<MatchNarrowCardBindingItem>>()
    val loadComplete = MutableLiveData<Boolean>().apply { value = false }
    private var initialLoadComplete = false

    init {
        getGroups()
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
        sortedGroups.value = result.sortedBy { it.id }
        loadInitialGroup()
        noDataAvailable.value = result.isEmpty()
    }

    fun loadInitialGroup() {
        if (!initialLoadComplete) {
            sortedGroups.value?.find { it.id == 1 }?.let { groupA ->
                textDropdownTitle.value = groupA.groupName
                loadGroupData(groupA)
                initialLoadComplete = true
            }
        }
    }

    fun loadGroupData(group: Group) {
        viewModelScope.launch {
            val teamList = group.teamsId.mapNotNull { teamRepository.getTeamById(it) }
            val groupTieBreaker = groupStageManager.calculateGroupTieBreakers(teamList)

            val sortedTeams = groupStageManager.sortTeamsByTieBreaker(groupTieBreaker)
            val groupMatches = getGroupMatches(group.groupName ?: "")

            teamsInSelectedGroup.postValue(sortedTeams)
            matchesInSelectedGroup.postValue(groupMatches)
            loadComplete.postValue(true)

            if (sortedTeams.all { it.played == 3 }) {
                updateAllGroupsRankings()
            }
        }
    }

    private fun getGroupMatches(groupName: String): List<MatchNarrowCardBindingItem> {
        return matches.filter { it.phase == groupName }
            .map { MatchNarrowCardBindingItem(it, teamRepository) }
    }

    private fun updateAllGroupsRankings() {
        viewModelScope.launch {
            val allGroupsRankings = sortedGroups.value?.mapNotNull { group ->
                val teamList = group.teamsId.mapNotNull { teamRepository.getTeamById(it) }
                val groupTieBreaker = groupStageManager.calculateGroupTieBreakers(teamList)
                val sortedTeams = groupStageManager.sortTeamsByTieBreaker(groupTieBreaker)
                group.groupName?.let { it to sortedTeams }
            }?.toMap()

            if (allGroupsRankings != null) {
                updateMatchesJson(allGroupsRankings)
            }
        }
    }

    private fun updateMatchesJson(sortedTeamsByGroup: Map<String, List<Team>>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                matchRepository.updateRoundOf16Matches(sortedTeamsByGroup)
            }
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}