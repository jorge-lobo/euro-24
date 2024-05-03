package com.example.euro24.ui.home.preTournament

import android.app.Application
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.DateUtils

class PreTournamentViewModel(application: Application) : BaseViewModel(application) {

    fun countDaysToTournament(): Int {
        val differenceInMillis = (DateUtils.datePreTournament?.time ?: 0) - DateUtils.currentDate.time
        val differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24)
        return differenceInDays.toInt()
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}