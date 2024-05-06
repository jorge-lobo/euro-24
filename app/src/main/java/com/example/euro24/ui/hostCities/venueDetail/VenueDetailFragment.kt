package com.example.euro24.ui.hostCities.venueDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.euro24.R
import com.example.euro24.data.venues.Venue
import com.example.euro24.databinding.FragmentVenueDetailBinding
import com.example.euro24.ui.common.BaseFragment

class VenueDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentVenueDetailBinding

    companion object {
        private const val ARG_VENUE = "venue"

        fun newInstance(venue: Venue): VenueDetailFragment {
            val fragment = VenueDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_VENUE, venue)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_venue_detail,
            container,
            false
        )

        return binding.root
    }

}