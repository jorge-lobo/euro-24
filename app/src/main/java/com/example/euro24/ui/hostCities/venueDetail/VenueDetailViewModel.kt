package com.example.euro24.ui.hostCities.venueDetail

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.euro24.R
import com.example.euro24.data.matches.Match
import com.example.euro24.data.matches.MatchRepository
import com.example.euro24.data.venues.VenueRepository
import com.example.euro24.ui.common.BaseViewModel
import com.example.euro24.utils.ImagesResourceMap
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class VenueDetailViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val venueRepository: VenueRepository = VenueRepository(application)
    private val matchRepository: MatchRepository = MatchRepository(application)

    private val defaultImage = R.drawable.default_image

    var hostCityNameEnglish = MutableLiveData<String>().apply { value = "" }
    var hostCityNameGerman = MutableLiveData<String>().apply { value = "" }
    var hostCityState = MutableLiveData<String>().apply { value = "" }
    var hostCityPopulation = MutableLiveData<String>().apply { value = "" }
    var hostCityElevation = MutableLiveData<String>().apply { value = "" }
    var stadiumName = MutableLiveData<String>().apply { value = "" }
    var stadiumAlias = MutableLiveData<String>().apply { value = "" }
    var stadiumCapacity = MutableLiveData<String>().apply { value = "" }
    var hostCityImageResourceId = MutableLiveData<Int>().apply { value = 0 }
    var stadiumImageResourceId = MutableLiveData<Int>().apply { value = 0 }

    private var matches = MutableLiveData<List<Match>?>()
    val sortedMatches = MutableLiveData<List<Match>?>()

    fun initialize(venueId: Int) {
        getVenueDetails(venueId)
        getVenueFixtures(venueId)
    }

    private fun getVenueDetails(venueId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            try {
                val venue = venueRepository.getVenueById(venueId)
                if (venue != null) {
                    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
                    hostCityNameEnglish.value = venue.cityEN
                    hostCityNameGerman.value = venue.cityDE
                    hostCityState.value = venue.state
                    hostCityPopulation.value = numberFormat.format(venue.population)
                    hostCityElevation.value = "${numberFormat.format(venue.elevation)} m"
                    stadiumName.value = venue.stadiumName
                    stadiumAlias.value = venue.stadiumAlias
                    stadiumCapacity.value = numberFormat.format(venue.capacity)

                    val hostCityImageResourceId =
                        ImagesResourceMap.cityImageResourceMap[venueId] ?: defaultImage
                    val stadiumImageResourceId =
                        ImagesResourceMap.stadiumImageResourceMap[venueId]
                            ?: defaultImage

                    this@VenueDetailViewModel.hostCityImageResourceId.value =
                        hostCityImageResourceId
                    this@VenueDetailViewModel.stadiumImageResourceId.value = stadiumImageResourceId
                } else {
                    onError("Venue not found")
                }
            } catch (e: Exception) {
                onError("Failed to fetch venue details: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun getVenueFixtures(venueId: Int) {
        isLoading.value = true
        noDataAvailable.value = false

        viewModelScope.launch {
            try {
                val result = matchRepository.getMatches().filter { it.venueId == venueId }
                handleMatchesResult(result)
            } catch (e: Exception) {
                onError(e.message)
            } finally {
                isLoading.value = false
                isRefreshing.value = false
            }
        }
    }

    private fun handleMatchesResult(result: List<Match>) {
        matches.value = result.sortedBy { it.id }
        sortedMatches.value = matches.value
        noDataAvailable.value = result.isEmpty()
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        handleError(message, validationErrors)
        isLoading.value = false
        isRefreshing.value = false
    }
}