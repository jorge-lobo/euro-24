package com.example.euro24.ui.teams.teamDetail.teamMatches

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.data.matches.Match
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.data.venues.VenueRepository
import com.example.euro24.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class TeamMatchesViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val matchRepository: MatchRepository = MatchRepository(application)
    private val venueRepository: VenueRepository = VenueRepository(application)

    private var matches = MutableLiveData<List<Match>?>()
    val sortedMatches = MutableLiveData<List<Match>?>()

    fun initialize(teamId: Int) {
        getTeamMatches(teamId)
    }

    private fun getTeamMatches(teamId: Int) {
        isLoading.value = true
        noDataAvailable.value = false

        viewModelScope.launch {
            try {
                val matchResult = matchRepository.getMatches()
                    .filter { it.team1Id == teamId || it.team2Id == teamId }
                handleMatchesResult(matchResult)
            } catch (e: Exception) {
                onError(e.message)
            } finally {
                isLoading.value = false
                isRefreshing.value = false
            }
        }
    }

    private fun handleMatchesResult(result: List<Match>) {
        val updatedMatches = result.map { match ->
            val venue = venueRepository.getVenueById(match.venueId ?: -1)
            match.copy(city = venue?.cityEN ?: "")
        }
        matches.value = updatedMatches.sortedBy { it.id }
        sortedMatches.value = matches.value
        noDataAvailable.value = result.isEmpty()
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}