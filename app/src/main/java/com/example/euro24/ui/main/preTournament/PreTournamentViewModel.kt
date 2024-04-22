package com.example.euro24.ui.main.preTournament

import android.app.Application
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.Utils

class PreTournamentViewModel(application: Application) : BaseViewModel(application) {

    fun countDaysToTournament(): Int {
        val differenceInMillis = (Utils.datePreTournament?.time ?: 0) - Utils.currentDate.time
        val differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24)
        return differenceInDays.toInt()
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}