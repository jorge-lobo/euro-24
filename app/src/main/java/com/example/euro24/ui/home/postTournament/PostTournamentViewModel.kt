package com.example.euro24.ui.home.postTournament

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.ui.common.BaseViewModel

class PostTournamentViewModel(application: Application) : BaseViewModel(application) {

    private val matchesRepository = MatchRepository(application)

    private val _championId = MutableLiveData<Int?>()
    val championId: LiveData<Int?> get() = _championId

    init {
        getChampion()
    }

    private fun getChampion() {
        val matchId = 51
        val match = matchesRepository.getMatchById(matchId)

        match?.let {
            val championId = if ((it.resultTeam1 ?: 0) > (it.resultTeam2 ?: 0)) {
                it.team1Id
            } else {
                it.team2Id
            }
            _championId.postValue(championId)
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}