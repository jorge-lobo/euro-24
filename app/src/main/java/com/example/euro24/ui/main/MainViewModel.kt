package com.example.euro24.ui.main

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.DateUtils

class MainViewModel(application: Application) : BaseViewModel(application) {

    private val connectivityManager = application.getSystemService(ConnectivityManager::class.java)

    enum class DateCondition {
        PRE_TOURNAMENT,
        DURING_TOURNAMENT,
        POST_TOURNAMENT
    }

    val dateCondition = MutableLiveData<DateCondition>()
    val internetConnection = MutableLiveData<Boolean>()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            internetConnection.postValue(true)
        }

        override fun onLost(network: Network) {
            internetConnection.postValue(false)
        }
    }

    fun checkInternetConnectivity() {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        val isConnected =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        internetConnection.postValue(isConnected)
    }

    fun getCurrentDate(): String {
        return DateUtils.formatDateLong(DateUtils.currentDate)
    }

    fun checkDateCondition() {
        when {
            DateUtils.currentDate.before(DateUtils.datePreTournament) -> dateCondition.postValue(DateCondition.PRE_TOURNAMENT)
            DateUtils.currentDate.after(DateUtils.datePostTournament) -> dateCondition.postValue(DateCondition.POST_TOURNAMENT)
            else -> dateCondition.postValue(DateCondition.DURING_TOURNAMENT)
        }
    }

    override fun onCleared() {
        super.onCleared()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
    }
}