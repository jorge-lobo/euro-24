package com.example.euro24.ui.matches.calendar

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.example.euro24.ui.common.BaseViewModel

class CalendarViewModel(application: Application) : BaseViewModel(application), LifecycleObserver {


    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}