package com.example.euro24.ui.hostCities

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.euro24.data.venues.Venue
import com.example.euro24.data.venues.VenueRepository
import com.example.euro24.ui.common.BaseViewModel

class HostCitiesViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val venueRepository: VenueRepository = VenueRepository(application)

    private val _venues = MutableLiveData<List<Venue>>()
    val venues: LiveData<List<Venue>> get() = _venues

    fun loadVenues() {
        val venueList = venueRepository.getVenues()
        _venues.postValue(venueList)
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}