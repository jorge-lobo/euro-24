package com.example.euro24.ui.matches.matchEditor.confirmation

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.R
import com.example.euro24.data.matches.Match
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.ImagesResourceMap
import kotlinx.coroutines.launch

class MatchEditorConfirmationViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val matchRepository: MatchRepository = MatchRepository(application)
    private val teamRepository: TeamRepository = TeamRepository(application)
    private val defaultFlag = R.drawable.default_flag

    var team1Id = MutableLiveData<Int>().apply { value = 0 }
    var team2Id = MutableLiveData<Int>().apply { value = 0 }
    var team1Name = MutableLiveData<String>().apply { value = "" }
    var team2Name = MutableLiveData<String>().apply { value = "" }
    var team1ImageResourceId = MutableLiveData<Int>().apply { value = 0 }
    var team2ImageResourceId = MutableLiveData<Int>().apply { value = 0 }


    fun initialize(matchId: Int) {
        getMatch(matchId)
    }

    private fun getMatch(matchId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            try {
                val match = matchRepository.getMatchById(matchId)
                match?.let { updateMatchDetails(it) } ?: onError("Match not found")
            } catch (e: Exception) {
                onError("Failed to fetch match details: ${e.message}")
            } finally {
                isLoading.value = false
                isRefreshing.value = false
            }
        }
    }

    private fun updateMatchDetails(match: Match) {
        val team1 = teamRepository.getTeamById(match.team1Id ?: 0)
        val team2 = teamRepository.getTeamById(match.team2Id ?: 0)

        team1Name.value = team1?.name ?: "Unknown"
        team2Name.value = team2?.name ?: "Unknown"

        team1ImageResourceId.value =
            team1?.id?.let { ImagesResourceMap.flagResourceMapById[it] } ?: defaultFlag
        team2ImageResourceId.value =
            team2?.id?.let { ImagesResourceMap.flagResourceMapById[it] } ?: defaultFlag
    }


    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
    }
}