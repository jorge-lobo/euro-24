package com.example.euro24.ui.teams.teamDetail.teamInfo

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.R
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.ImagesResourceMap
import kotlinx.coroutines.launch

class TeamInfoViewModel(application: Application) : BaseViewModel(application), LifecycleObserver {

    private val teamRepository: TeamRepository = TeamRepository(application)
    private val defaultImage = R.drawable.default_image

    var teamNicknameOg = MutableLiveData<String>().apply { value = "" }
    var teamNicknameEng = MutableLiveData<String>().apply { value = "" }
    var teamFifaCode = MutableLiveData<String>().apply { value = "" }
    var teamKitBrand = MutableLiveData<String>().apply { value = "" }
    var teamNumberAppearances = MutableLiveData<Int>().apply { value = 0 }
    var teamPreviousAppearances = MutableLiveData<String>().apply { value = "" }
    var teamNumberTitles = MutableLiveData<Int>().apply { value = 0 }
    var teamBestResult = MutableLiveData<String>().apply { value = "" }
    var teamDateQualification = MutableLiveData<String>().apply { value = "" }
    var teamQualifiedAs = MutableLiveData<String>().apply { value = "" }
    var teamPhotoResourceId = MutableLiveData<Int>().apply { value = 0 }
    var teamCrestResourceId = MutableLiveData<Int>().apply { value = 0 }

    fun initialize(teamId: Int) {
        getTeamDetails(teamId)
    }

    private fun getTeamDetails(teamId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            try {
                val team = teamRepository.getTeamById(teamId)
                if (team != null) {
                    teamNicknameOg.value = team.nicknameOG
                    teamNicknameEng.value = team.nicknameEN
                    teamFifaCode.value = team.fifaCode
                    teamKitBrand.value = team.kitBrand
                    teamNumberAppearances.value = team.numberAppearances
                    teamPreviousAppearances.value = team.previousAppearances
                    teamNumberTitles.value = team.titles
                    teamBestResult.value = team.bestResult
                    teamDateQualification.value = team.qualificationDate
                    teamQualifiedAs.value = team.qualifiedAs

                    val teamPhotoResourceId =
                        ImagesResourceMap.teamImageResourceMapById[teamId] ?: defaultImage
                    val teamCrestResourceId =
                        ImagesResourceMap.crestResourceMapById[teamId] ?: defaultImage

                    this@TeamInfoViewModel.teamPhotoResourceId.value = teamPhotoResourceId
                    this@TeamInfoViewModel.teamCrestResourceId.value = teamCrestResourceId
                } else {
                    onError("Team not found")
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