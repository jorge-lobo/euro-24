package com.example.euro24.ui.pastFinals

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.data.pastFinals.PastFinal
import com.example.euro24.data.pastFinals.PastFinalRepository
import com.example.euro24.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class PastFinalsViewModel(application: Application) : BaseViewModel(application), LifecycleObserver {

private val pastFinalRepository = PastFinalRepository(application)

    private var pastFinals = MutableLiveData<List<PastFinal>?>()
    val sortedFinals = MutableLiveData<List<PastFinal>?>()

    init {
        getPastFinals()
    }

    private fun getPastFinals() {
        isLoading.value = true

        noDataAvailable.value = false

        viewModelScope.launch {
            try {
                val result = pastFinalRepository.getPastFinals()
                handlePastFinalsResult(result)
            } catch (e: Exception) {
                onError(e.message)
            } finally {
                isLoading.value = false
                isRefreshing.value = false
            }
        }
    }

    private fun handlePastFinalsResult(result: List<PastFinal>) {
        pastFinals.value = result.sortedBy { it.year }
        sortedFinals.value = pastFinals.value
        noDataAvailable.value = result.isEmpty()
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
        pastFinals.value = arrayListOf()
    }
}