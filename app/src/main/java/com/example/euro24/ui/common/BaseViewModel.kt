package com.example.euro24.ui.common

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.euro24.MyApplication
import com.example.euro24.R
import com.example.euro24.data.error.IErrorCallback

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    IErrorCallback {

    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
    }

    private var sessionState = MutableLiveData<AuthenticationState>().apply { value = null }
    val app = application
    val isRefreshing = MutableLiveData<Boolean>().apply { value = false }
    val isLoading = MutableLiveData<Boolean>().apply { value = false }
    val noDataAvailable = MutableLiveData<Boolean>().apply { value = false }
    val errorMessage = MutableLiveData<String?>()
//    val displayErrorLayout = MutableLiveData<Boolean>().apply { value = false }
//    private val genericErrorMessage = MyApplication.getAppContext().getString(R.string.error_generic_message)

    abstract fun onError(message: String? = null, validationErrors: Map<String, ArrayList<String>>? = null)

    override fun onGenericError(
        message: String?,
        validationErrors: Map<String, ArrayList<String>>?
    ) {
        if (validationErrors == null) {
            errorMessage.postValue(message)
        }
        onError(message, validationErrors)
    }

    override fun onTimeout() {
        // API Call Timeout
        errorMessage.postValue(MyApplication.getAppContext().getString(R.string.error_network_server_timeout))
        onError()
    }

    override fun onNetworkError() {
        // Check your connection
        errorMessage.postValue(MyApplication.getAppContext().getString(R.string.error_network_error))
        onError()
    }

    override fun onSessionExpired() {
        sessionState.value = AuthenticationState.UNAUTHENTICATED
    }

    protected fun handleError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        // Common error handling logic
        // You can customize this method based on your application's needs
        // For example, you might want to show a generic error message or handle specific errors differently.
        // You can also use LiveData to notify the UI about the error.

        // Log the error for debugging purposes
        Log.e("BaseViewModel", "Error: $message")
    }
}