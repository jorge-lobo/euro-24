package com.example.euro24.ui.hostCities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.data.venues.Venue
import com.example.euro24.databinding.FragmentHostCitiesBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.hostCities.venueDetail.VenueDetailFragment

class HostCitiesFragment : BaseFragment(), HostCityGridAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHostCitiesBinding
    private val mHostCitiesViewModel by lazy { ViewModelProvider(this)[HostCitiesViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_host_cities,
            container,
            false
        )
        binding.viewModel = mHostCitiesViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mHostCitiesViewModel.loadVenues()
        setupObservers()
    }

    override fun onItemClick(venue: Venue) {
        val fragment = VenueDetailFragment.newInstance(venue)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupObservers() {
        mHostCitiesViewModel.venues.observe(viewLifecycleOwner) { venues ->
            venues?.let {
                val sortedVenueList = it.sortedBy { venue -> venue.cityEN }
                val adapter = HostCityGridAdapter(requireContext(), sortedVenueList, this)
                binding.gridHostCities.adapter = adapter
            }
        }
    }

}