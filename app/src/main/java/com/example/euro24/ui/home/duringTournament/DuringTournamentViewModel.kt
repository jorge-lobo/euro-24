package com.example.euro24.ui.home.duringTournament

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.data.matches.Match
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.DateUtils
import kotlinx.coroutines.launch

class DuringTournamentViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val matchesRepository = MatchRepository(application)

    private var matches = MutableLiveData<List<Match>?>()
    val sortedMatches = MutableLiveData<List<Match>?>()

    init {
        getMatches()
    }

    private fun getMatches() {
        isLoading.value = true

        val date = DateUtils.currentDate

        noDataAvailable.value = false

        viewModelScope.launch {
            try {
                val result = matchesRepository.getMatches().filter { it.date == date.toString() }
                handleMatchesResult(result)
            } catch (e: Exception) {
                onError(e.message)
            } finally {
                isLoading.value = false
                isRefreshing.value = false
            }
        }
    }

    private fun handleMatchesResult(result: List<Match>) {
        matches.value = result.sortedBy { it.time }
        sortedMatches.value = matches.value
        noDataAvailable.value = result.isEmpty()
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
        matches.value = arrayListOf()
    }
}