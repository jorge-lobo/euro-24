package com.example.euro24.ui.matches.matchEditor

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.R
import com.example.euro24.data.matches.Match
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

    var team1Id = MutableLiveData<Int>().apply { value = 0 }
    var team2Id = MutableLiveData<Int>().apply { value = 0 }
    var team1Name = MutableLiveData<String>().apply { value = "" }
    var team2Name = MutableLiveData<String>().apply { value = "" }
    var team1Score = MutableLiveData<Int>().apply { value = -1 }
    var team2Score = MutableLiveData<Int>().apply { value = -1 }
    var team1ExtraTime = MutableLiveData<Int>().apply { value = -1 }
    var team2ExtraTime = MutableLiveData<Int>().apply { value = -1 }
    var team1Penalties = MutableLiveData<Int>().apply { value = 0 }
    var team2Penalties = MutableLiveData<Int>().apply { value = 0 }
    var team1ImageResourceId = MutableLiveData<Int>().apply { value = 0 }
    var team2ImageResourceId = MutableLiveData<Int>().apply { value = 0 }

    var team1ResultDecreaseButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    var team2ResultDecreaseButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    var team1ExtraTimeDecreaseButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    var team2ExtraTimeDecreaseButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    var team1PenaltiesDecreaseButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    var team2PenaltiesDecreaseButtonEnabled = MutableLiveData<Boolean>().apply { value = false }

    val editionFinished = MutableLiveData<Boolean>().apply { value = false }
    val dateCondition = MutableLiveData<DateCondition>()

    val resultTied = MutableLiveData<Boolean>().apply { value = false }
    val extraTimeTied = MutableLiveData<Boolean>().apply { value = false }
    val penaltiesTied = MutableLiveData<Boolean>().apply { value = false }

    val savedButtonEnabled = MediatorLiveData<Boolean>().apply {
        listOf(
            team1Score,
            team2Score,
            team1ExtraTime,
            team2ExtraTime,
            team1Penalties,
            team2Penalties
        ).forEach {
            addSource(it) { checkSaveButtonState() }
        }
    }

    private var currentScoreType = ScoreType.RESULT

    enum class DateCondition {
        GROUP_STAGE,
        KNOCKOUT
    }

    enum class ScoreType {
        RESULT,
        EXTRA_TIME,
        PENALTIES
    }

    init {
        observeScoreChanges()
    }

    private fun checkSaveButtonState() {
        val isScoreValid = { score: Int? -> score != null && score >= 0 }
        savedButtonEnabled.value = when (currentScoreType) {
            ScoreType.RESULT -> isScoreValid(team1Score.value) && isScoreValid(team2Score.value)
            ScoreType.EXTRA_TIME -> isScoreValid(team1ExtraTime.value) &&
                    isScoreValid(team2ExtraTime.value)

            ScoreType.PENALTIES -> isScoreValid(team1Penalties.value) &&
                    isScoreValid(team2Penalties.value)
        }
    }

    fun handleSaveButton() {
        when (dateCondition.value) {
            DateCondition.GROUP_STAGE -> handleGroupStage()
            DateCondition.KNOCKOUT, null -> handleKnockoutStage()
        }
    }

    private fun handleGroupStage() {
        editionFinished.value = true
    }

    private fun handleKnockoutStage() {
        when (currentScoreType) {
            ScoreType.RESULT -> if (team1Score.value != team2Score.value) editionFinished.value =
                true else proceedToNextStage(resultTied, ScoreType.EXTRA_TIME)

            ScoreType.EXTRA_TIME -> if (team1ExtraTime.value != team2ExtraTime.value) editionFinished.value =
                true else proceedToNextStage(extraTimeTied, ScoreType.PENALTIES)

            ScoreType.PENALTIES -> if (team1Penalties.value != team2Penalties.value) editionFinished.value =
                true else penaltiesTied.value = true
        }
    }

    private fun proceedToNextStage(tied: MutableLiveData<Boolean>, nextScoreType: ScoreType) {
        tied.value = true
        currentScoreType = nextScoreType
        checkSaveButtonState()

        if (nextScoreType == ScoreType.EXTRA_TIME) {
            team1ExtraTime.value = team1Score.value ?: - 1
            team2ExtraTime.value = team2Score.value ?: - 1
        }
    }

    private fun observeScoreChanges() {
        listOf(
            team1Score to team1ResultDecreaseButtonEnabled,
            team2Score to team2ResultDecreaseButtonEnabled,
            team1Penalties to team1PenaltiesDecreaseButtonEnabled,
            team2Penalties to team2PenaltiesDecreaseButtonEnabled
        ).forEach { (score, button) ->
            score.observeForever { value ->
                val isEnabled = (value ?: 0) > 0
                button.value = isEnabled
            }
        }

        listOf(team1ExtraTime to team1ExtraTimeDecreaseButtonEnabled,
            team2ExtraTime to team2ExtraTimeDecreaseButtonEnabled
        ).forEach { (score, button) ->
            score.observeForever { value ->
                val isEnabled = (value ?: 0) > (team1Score.value?.toInt() ?: 0)
                button.value = isEnabled
            }
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

    fun checkDateCondition() {
        /*val date = DateUtils.currentDate*/
        val date = DateUtils.formatter.parse("28/06/2024")
        dateCondition.value =
            if (date != null && date.before(DateUtils.dateStartKnockout)) DateCondition.GROUP_STAGE else DateCondition.KNOCKOUT
    }

    fun changeScore(team: Int, type: ScoreType, increase: Boolean) {
        val score = when (type) {
            ScoreType.RESULT -> if (team == 1) team1Score else team2Score
            ScoreType.EXTRA_TIME -> if (team == 1) team1ExtraTime else team2ExtraTime
            ScoreType.PENALTIES -> if (team == 1) team1Penalties else team2Penalties
        }
        updateScore(score, increase)
    }

    private fun updateScore(score: MutableLiveData<Int>, increase: Boolean) {
        score.value = score.value?.let { if (increase) it + 1 else maxOf(0, it - 1) }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}