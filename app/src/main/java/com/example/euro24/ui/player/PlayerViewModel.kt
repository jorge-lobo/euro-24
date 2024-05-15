package com.example.euro24.ui.player

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.R
import com.example.euro24.data.players.PlayerRepository
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.ImagesResourceMap
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PlayerViewModel(application: Application) : BaseViewModel(application), LifecycleObserver {

    private val playerRepository: PlayerRepository = PlayerRepository(application)

    var playerName = MutableLiveData<String>().apply { value = "" }
    var playerNumber = MutableLiveData<Int>().apply { value = 0 }
    var playerFullName = MutableLiveData<String>().apply { value = "" }
    var playerBirthDay = MutableLiveData<String>().apply { value = "" }
    var playerAge = MutableLiveData<String>().apply { value = "" }
    var playerBirthplace = MutableLiveData<String>().apply { value = "" }
    var playerHeight = MutableLiveData<String>().apply { value = "" }
    var playerWeight = MutableLiveData<String>().apply { value = "" }
    var playerFoot = MutableLiveData<String>().apply { value = "" }
    var playerPosition = MutableLiveData<String>().apply { value = "" }
    var playerCaps = MutableLiveData<Int>().apply { value = 0 }
    var playerGoals = MutableLiveData<Int>().apply { value = 0 }
    var playerDebutDate = MutableLiveData<String>().apply { value = "" }
    var playerDebutOpponent = MutableLiveData<String>().apply { value = "" }
    var playerClubName = MutableLiveData<String>().apply { value = "" }
    var playerLeagueName = MutableLiveData<String>().apply { value = "" }
    var flagImageResourceId = MutableLiveData<Int>().apply { value = 0 }
    var playerPhotoImageUrl = MutableLiveData<String>().apply { value = "" }
    var clubCrestImageUrl = MutableLiveData<String>().apply { value = "" }

    fun initialize(playerId: Int) {
        getPlayer(playerId)
    }

    private fun getPlayer(playerId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            try {
                val player = playerRepository.getPlayerById(playerId)
                if (player != null) {
                    playerName.value = player.shortName
                    playerNumber.value = player.number
                    playerFullName.value = player.fullName
                    playerBirthDay.value = formatBirthDate(player.dob)
                    playerBirthplace.value = player.placeBirth
                    playerHeight.value = "${player.height} m"
                    playerWeight.value = "${player.weight} kg"
                    playerFoot.value = player.foot
                    playerPosition.value = player.position
                    playerCaps.value = player.caps
                    playerGoals.value = player.goals
                    playerDebutDate.value = player.debutDate
                    playerDebutOpponent.value = player.debutOpponent
                    playerClubName.value = player.club
                    playerLeagueName.value = player.league
                    playerPhotoImageUrl.value = player.image
                    clubCrestImageUrl.value = player.clubCrest

                    val age = calculateAge(player.dob ?: "")
                    playerAge.value = "$age years"

                    val flagImageResourceId =
                        ImagesResourceMap.flagResourceMapById[player.teamId]
                            ?: R.drawable.default_flag

                    this@PlayerViewModel.flagImageResourceId.value = flagImageResourceId

                } else {
                    onError("Player not found")
                }
            } catch (e: Exception) {
                onError("Failed to fetch player details: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun formatBirthDate(birthDate: String?): String {
        birthDate ?: return ""
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        val date = inputFormat.parse(birthDate)
        return date?.let { outputFormat.format(it) } ?: ""
    }

    private fun calculateAge(birthDate: String): Int {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateOfBirth = formatter.parse(birthDate)
        val calendar = Calendar.getInstance()
        val today = calendar.time
        if (dateOfBirth != null) {
            calendar.time = dateOfBirth
        }
        val birthYear = calendar.get(Calendar.YEAR)
        calendar.time = today
        val currentYear = calendar.get(Calendar.YEAR)
        var age = currentYear - birthYear
        if (calendar.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}