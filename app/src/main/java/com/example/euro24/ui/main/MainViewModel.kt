package com.example.euro24.ui.main

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.DateUtils

class MainViewModel(application: Application) : BaseViewModel(application) {

    private val connectivityManager = application.getSystemService(ConnectivityManager::class.java)
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            internetConnection.postValue(true)
        }

        override fun onLost(network: Network) {
            internetConnection.postValue(false)
        }
    }

    val dateCondition = MutableLiveData<DateCondition>()
    val internetConnection = MutableLiveData<Boolean>()

    var isNetworkCallbackRegistered = false

    enum class DateCondition {
        PRE_TOURNAMENT,
        DURING_TOURNAMENT,
        POST_TOURNAMENT
    }

    init {
        registerNetworkCallback()
    }
    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
        isNetworkCallbackRegistered = true
    }

    private fun unregisterNetworkCallback() {
        if (isNetworkCallbackRegistered) {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
            isNetworkCallbackRegistered = false
        }
    }

    fun checkInternetConnectivity() {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        val isConnected =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        internetConnection.postValue(isConnected)
    }

    fun setInternetConnection(isConnected: Boolean) {
        internetConnection.postValue(isConnected)
    }

    fun getCurrentDate(): String {
        return DateUtils.formatDateLong(DateUtils.currentDate)
    }

    fun checkDateCondition() {
        dateCondition.postValue(
            when {
                DateUtils.currentDate.before(DateUtils.datePreTournament) -> DateCondition.PRE_TOURNAMENT
                DateUtils.currentDate.after(DateUtils.datePostTournament) -> DateCondition.POST_TOURNAMENT
                else -> DateCondition.DURING_TOURNAMENT
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        unregisterNetworkCallback()
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
    }
}