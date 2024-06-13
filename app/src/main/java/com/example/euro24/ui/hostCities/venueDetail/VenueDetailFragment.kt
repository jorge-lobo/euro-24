package com.example.euro24.ui.hostCities.venueDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.databinding.FragmentVenueDetailBinding
import com.example.euro24.ui.common.BaseFragment

class VenueDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentVenueDetailBinding
    private val mVenueDetailViewModel by lazy { ViewModelProvider(this)[VenueDetailViewModel::class.java] }
    private lateinit var teamRepository: TeamRepository
    private lateinit var venueMatchesAdapter: VenueMatchesAdapter

    companion object {
        private const val ARG_VENUE_ID = "venue_id"

        fun newInstance(venueId: Int): VenueDetailFragment {
            val fragment = VenueDetailFragment()
            val args = Bundle()
            args.putInt(ARG_VENUE_ID, venueId)
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

        binding.viewModel = mVenueDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val venueId = arguments?.getInt(ARG_VENUE_ID) ?: 0
        mVenueDetailViewModel.initialize(venueId)

        teamRepository = TeamRepository(requireContext())
        venueMatchesAdapter = VenueMatchesAdapter(teamRepository)

        setupViews()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupViews() {
        val logoColor = R.color.venue_detail_header_logo
        binding.wordMarkVenueDetail.headerLogo.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                logoColor
            )
        )
    }

    private fun setupRecyclerView() {
        binding.rvVenueMatches.apply {
            adapter = venueMatchesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        with(mVenueDetailViewModel) {
            hostCityImageResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.imageHostCityBanner.setImageResource(resourceId)
            }

            stadiumImageResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.imageStadium.setImageResource(resourceId)
            }

            sortedMatches.observe(viewLifecycleOwner) { matches ->
                matches?.let {
                    val matchCardNarrowItems =
                        matches.map { VenueMatchesBindingItem(it, teamRepository) }
                    venueMatchesAdapter.submitList(matchCardNarrowItems)
                }
            }
        }
    }
}