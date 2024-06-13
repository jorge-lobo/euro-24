package com.example.euro24.ui.home.preTournament

import android.app.Application
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.DateUtils
import java.util.Calendar

class PreTournamentViewModel(application: Application) : BaseViewModel(application) {

    fun countDaysToTournament(): Int {
        val startOfToday = Calendar.getInstance().apply {
            time = DateUtils.currentDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val startOfPreTournament = Calendar.getInstance().apply {
            time = DateUtils.datePreTournament!!
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val differenceInMillis = startOfPreTournament.time - startOfToday.time
        val differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24)
        return differenceInDays.toInt()
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}