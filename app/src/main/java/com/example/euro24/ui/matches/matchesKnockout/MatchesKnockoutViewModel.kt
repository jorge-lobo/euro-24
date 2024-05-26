package com.example.euro24.ui.matches.matchesKnockout

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.ui.matches.MatchNarrowCardBindingItem
import com.example.euro24.utils.DateUtils
import kotlinx.coroutines.launch

class MatchesKnockoutViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val matchRepository: MatchRepository = MatchRepository(application)
    private val teamRepository: TeamRepository = TeamRepository(application)
    val dateCondition = MutableLiveData<DateCondition>()
    val matchesInSelectedRound = MutableLiveData<List<MatchNarrowCardBindingItem>>()

    enum class DateCondition {
        ROUND_OF_16,
        QUARTER_FINALS,
        SEMI_FINALS,
        FINAL
    }

    init {
        checkDateCondition()
    }

    fun checkDateCondition() {
        val currentPhase = when {
            DateUtils.currentDate.before(DateUtils.dateEndRoundOf16) -> DateCondition.ROUND_OF_16
            DateUtils.currentDate.before(DateUtils.dateEndQuarterFinals) -> DateCondition.QUARTER_FINALS
            DateUtils.currentDate.before(DateUtils.dateEndSemiFinals) -> DateCondition.SEMI_FINALS
            else -> DateCondition.FINAL
        }
        dateCondition.postValue(currentPhase)
        filterMatchesByPhase(currentPhase)
    }

    fun filterMatchesByPhase(phase: DateCondition) {
        viewModelScope.launch {
            val filteredMatches = matchRepository.getMatches().filter { match ->
                match.phase.equals(phase.toPhaseString(), ignoreCase = true)
            }
            matchesInSelectedRound.postValue(filteredMatches.map { MatchNarrowCardBindingItem(it, teamRepository) })
        }
    }

    private fun DateCondition.toPhaseString(): String {
        return when (this) {
            DateCondition.ROUND_OF_16 -> "Round of 16"
            DateCondition.QUARTER_FINALS -> "Quarter-finals"
            DateCondition.SEMI_FINALS -> "Semi-finals"
            DateCondition.FINAL -> "Final"
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}