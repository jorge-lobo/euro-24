package com.example.euro24.ui.teams.teamDetail.teamSquad

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.data.managers.Manager
import com.example.euro24.data.managers.ManagerRepository
import com.example.euro24.data.players.Player
import com.example.euro24.data.players.PlayerRepository
import com.example.euro24.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class TeamSquadViewModel(application: Application) : BaseViewModel(application), LifecycleObserver {

    private val playerRepository = PlayerRepository(application)
    private val managerRepository = ManagerRepository(application)

    val forwards = MutableLiveData<List<Player>>()
    val midfielders = MutableLiveData<List<Player>>()
    val defenders = MutableLiveData<List<Player>>()
    val goalkeepers = MutableLiveData<List<Player>>()
    val headManager = MutableLiveData<Manager>()

    fun initialize(teamId: Int) {
        getTeamSquad(teamId)
        getHeadManager(teamId)
    }

    private fun getTeamSquad(teamId: Int) {
        isLoading.value = true
        noDataAvailable.value = false

        viewModelScope.launch {
            try {
                val result = playerRepository.getPlayers().filter { it.teamId == teamId }
                handleSquadResult(result)
            } catch (e: Exception) {
                onError(e.message)
            } finally {
                isLoading.value = false
                isRefreshing.value = false
            }
        }
    }

    private fun getHeadManager(teamId: Int) {
        isLoading.value = true
        noDataAvailable.value = false

        viewModelScope.launch {
            try {
                headManager.value = managerRepository.getManagerByTeamId(teamId)
            } catch (e: Exception) {
                onError(e.message)
            } finally {
                isLoading.value = false
                isRefreshing.value = false
            }
        }
    }

    private fun handleSquadResult(result: List<Player>) {
        val groupedPlayers = result.groupBy { it.position }

        forwards.value = sortedPlayers(groupedPlayers["Forward"])
        midfielders.value = sortedPlayers(groupedPlayers["Midfielder"])
        defenders.value = sortedPlayers(groupedPlayers["Defender"])
        goalkeepers.value = sortedPlayers(groupedPlayers["Goalkeeper"])

        noDataAvailable.value = result.isEmpty()
    }

    private fun sortedPlayers(players: List<Player>?): List<Player>? {
        return players?.sortedBy { it.number }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}