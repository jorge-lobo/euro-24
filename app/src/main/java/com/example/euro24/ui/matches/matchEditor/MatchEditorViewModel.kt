package com.example.euro24.ui.matches.matchEditor

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.R
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.DateUtils
import com.example.euro24.utils.ImagesResourceMap
import kotlinx.coroutines.launch

class MatchEditorViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val matchRepository: MatchRepository = MatchRepository(application)
    private val teamRepository: TeamRepository = TeamRepository(application)
    private val defaultFlag = R.drawable.default_flag

    var team1Name = MutableLiveData<String>().apply { value = "" }
    var team2Name = MutableLiveData<String>().apply { value = "" }
    var team1Score = MutableLiveData<Int>().apply { value = -1 }
    var team2Score = MutableLiveData<Int>().apply { value = -1 }
    var team1ExtraTime = MutableLiveData<Int>().apply { value = -1 }
    var team2ExtraTime = MutableLiveData<Int>().apply { value = -1 }
    var team1Penalties = MutableLiveData<Int>().apply { value = -1 }
    var team2Penalties = MutableLiveData<Int>().apply { value = -1 }
    var team1ImageResourceId = MutableLiveData<Int>().apply { value = 0 }
    var team2ImageResourceId = MutableLiveData<Int>().apply { value = 0 }
    var broadcast = MutableLiveData<String>().apply { value = "" }

    var team1ResultDecreaseButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    var team2ResultDecreaseButtonEnabled = MutableLiveData<Boolean>().apply { value = false }

    val resultsTied = MediatorLiveData<Boolean>().apply {
        addSource(team1Score) { checkScores() }
        addSource(team2Score) { checkScores() }
    }

    val dateCondition = MutableLiveData<DateCondition>()
    val broadcaster = MutableLiveData<Broadcaster>()

    enum class DateCondition {
        GROUP_STAGE,
        KNOCKOUT
    }

    enum class Broadcaster {
        RTP,
        SIC,
        TVI,
        NONE
    }

    init {
        team1Score.observeForever {
            team1ResultDecreaseButtonEnabled.value = it > 0
        }

        team2Score.observeForever {
            team2ResultDecreaseButtonEnabled.value = it > 0
        }
    }

    fun initialize(matchId: Int) {
        getMatch(matchId)
    }

    private fun getMatch(matchId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            try {
                val match = matchRepository.getMatchById(matchId)
                if (match != null) {
                    val team1 = teamRepository.getTeamById(match.team1Id ?: 0)
                    val team2 = teamRepository.getTeamById(match.team2Id ?: 0)

                    team1Name.value = team1?.name ?: "Unknown"
                    team2Name.value = team2?.name ?: "Unknown"

                    val team1FlagResId =
                        team1?.id?.let { ImagesResourceMap.flagResourceMapById[it] } ?: defaultFlag
                    val team2FlagResId =
                        team2?.id?.let { ImagesResourceMap.flagResourceMapById[it] } ?: defaultFlag

                    this@MatchEditorViewModel.team1ImageResourceId.value = team1FlagResId
                    this@MatchEditorViewModel.team2ImageResourceId.value = team2FlagResId

                    broadcast.value = match.broadcastPT
                    checkBroadcaster()


                } else {
                    onError("Match not found")
                }
            } catch (e: Exception) {
                onError("Failed to fetch match details: ${e.message}")
            } finally {
                isLoading.value = false
                isRefreshing.value = false
            }
        }
    }

    fun checkDateCondition() {
        dateCondition.postValue(
            when {
                DateUtils.currentDate.before(DateUtils.dateStartKnockout) -> DateCondition.GROUP_STAGE
                else -> DateCondition.KNOCKOUT
            }
        )
    }

    private fun checkBroadcaster() {
        broadcaster.postValue(
            when (broadcast.value) {
                "RTP" -> Broadcaster.RTP
                "SIC" -> Broadcaster.SIC
                "TVI" -> Broadcaster.TVI
                else -> Broadcaster.NONE
            }
        )
    }

    private fun checkScores() {
        resultsTied.value = (team1Score.value == team2Score.value) &&
                (team1Score.value ?: -1) >= 0 && (team2Score.value ?: -1) >= 0
    }

    fun increaseTeam1Score() {
        team1Score.value = (team1Score.value ?: 0) + 1
    }

    fun decreaseTeam1Score() {
        team1Score.value = (team1Score.value ?: 0).let {
            if (it > 0) it - 1 else 0
        }
    }

    fun increaseTeam2Score() {
        team2Score.value = (team2Score.value ?: 0) + 1
    }

    fun decreaseTeam2Score() {
        team2Score.value = (team2Score.value ?: 0).let {
            if (it > 0) it - 1 else 0
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}