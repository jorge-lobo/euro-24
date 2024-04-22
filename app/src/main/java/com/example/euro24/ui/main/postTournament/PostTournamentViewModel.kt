package com.example.euro24.ui.main.postTournament

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.euro24.data.matches.Match
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.ui.common.BaseViewModel

class PostTournamentViewModel(application: Application) : BaseViewModel(application) {

    private val matchesRepository = MatchRepository(application)

    private val _team1Result = MutableLiveData<Int>()
    private val _team2Result = MutableLiveData<Int>()
    private val _championName = MutableLiveData<String?>()

    private val team1Result: LiveData<Int> get() = _team1Result
    private val team2Result: LiveData<Int> get() = _team2Result
    val championName: LiveData<String?> get() = _championName

    init {
        getChampion()
    }

    private fun getChampion() {
        val matchId = 51
        val match = matchesRepository.getMatchById(matchId)

        val isTeam1Champion = (team1Result.value ?: 0) > (team2Result.value ?: 0)

        val championName = if (isTeam1Champion) {
            match?.team1
        } else {
            match?.team2
        }

        _championName.postValue(championName)
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}