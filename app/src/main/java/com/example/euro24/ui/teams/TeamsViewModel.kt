package com.example.euro24.ui.teams

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.euro24.data.teams.Team
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.ui.common.BaseViewModel

class TeamsViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

        private val teamRepository: TeamRepository = TeamRepository(application)

    private val _teams = MutableLiveData<List<Team>>()
    val teams: LiveData<List<Team>> get() = _teams

    fun loadTeams() {
        val teamList = teamRepository.getTeams()
        _teams.postValue(teamList)
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}