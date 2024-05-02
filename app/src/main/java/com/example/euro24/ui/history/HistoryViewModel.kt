package com.example.euro24.ui.history

import android.app.Application
import com.example.euro24.ui.common.BaseViewModel

class HistoryViewModel(application: Application) : BaseViewModel(application) {



    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
    }
}