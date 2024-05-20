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
import kotlinx.coroutines.launch

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
            val sortedTeams = teamList.sortedByDescending { it.points }
            teamsInSelectedGroup.postValue(sortedTeams)
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}