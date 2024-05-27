package com.example.euro24.ui.matches.matchEditor

import android.app.Application
import com.example.euro24.ui.common.BaseViewModel

class MatchEditorViewModel(application: Application) : BaseViewModel(application) {



    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}