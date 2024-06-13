package com.example.euro24.ui.splash

import android.app.Application
import com.example.euro24.ui.common.BaseViewModel
import java.io.File
import java.io.FileOutputStream

class SplashScreenViewModel(application: Application) : BaseViewModel(application) {

    fun uploadJsonToInternalStorage() {
        uploadFile("matches.json")
        uploadFile("teams.json")
    }

    private fun uploadFile(filename: String) {
        val outputFile = File(getApplication<Application>().filesDir, filename)
        if (!outputFile.exists()) {
            val assetManager = getApplication<Application>().assets
            val inputStream = assetManager.open(filename)
            val outputStream = FileOutputStream(outputFile)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}