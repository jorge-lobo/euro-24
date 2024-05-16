package com.example.euro24.ui.matches

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.DateUtils

class MatchesViewModel(application: Application) : BaseViewModel(application) {

    val dateCondition = MutableLiveData<DateCondition>()

    enum class DateCondition {
        GROUP_STAGE,
        KNOCKOUT
    }

 fun checkDateCondition() {
     dateCondition.postValue(
         when {
             DateUtils.currentDate.before(DateUtils.dateStartKnockout) -> DateCondition.GROUP_STAGE
             DateUtils.currentDate.after(DateUtils.dateStartKnockout) -> DateCondition.KNOCKOUT
             else -> DateCondition.GROUP_STAGE
         }
     )
 }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}