package com.example.euro24.ui.teams.teamDetail

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.R
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.ImagesResourceMap
import kotlinx.coroutines.launch

class TeamDetailViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val teamRepository: TeamRepository = TeamRepository(application)

    val teamName = MutableLiveData<String>().apply { value = "" }
    val teamFlagResourceId = MutableLiveData<Int>().apply { value = 0 }

    fun initialize(teamId: Int) {
        getTeamDetails(teamId)
    }

    private fun getTeamDetails(teamId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            try {
                val team = teamRepository.getTeamById(teamId)
                if (team != null) {
                    teamName.value = team.name

                    val teamFlagResourceId =
                        ImagesResourceMap.flagResourceMapById[teamId] ?: R.drawable.default_flag

                    this@TeamDetailViewModel.teamFlagResourceId.value = teamFlagResourceId
                }
            } catch (e: Exception) {
                onError("Failed to fetch team details: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}