package com.example.euro24.ui.matches.calendar.selectedDay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.databinding.FragmentSelectedDayBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.home.duringTournament.DuringTournamentFragment
import com.example.euro24.ui.home.duringTournament.MatchCardAdapter
import com.example.euro24.ui.home.duringTournament.MatchCardBindingItem
import com.example.euro24.utils.DateUtils

class SelectedDayFragment : BaseFragment() {

    private lateinit var binding: FragmentSelectedDayBinding
    private val mSelectedDayViewModel by lazy { ViewModelProvider(this)[SelectedDayViewModel::class.java] }
    private lateinit var teamRepository: TeamRepository
    private lateinit var matchCardAdapter: MatchCardAdapter

    companion object {
        private const val ARG_DAY = "day"
        private const val ARG_MONTH = "month"

        fun newInstance(day: Int, month: Int): SelectedDayFragment {
            val fragment = SelectedDayFragment()
            val args = Bundle()
            args.putInt(ARG_DAY, day)
            args.putInt(ARG_MONTH, month)
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
            R.layout.fragment_selected_day,
            container,
            false
        )

        binding.viewModel = mSelectedDayViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamRepository = TeamRepository(requireContext())
        matchCardAdapter = MatchCardAdapter(teamRepository, DuringTournamentFragment(), this)

        val day = arguments?.getInt(ARG_DAY)
        val month = arguments?.getInt(ARG_MONTH)

        val selectedDay = if (day != null && month != null) {
            val monthPlusOne = month + 1
            String.format("%02d/%02d/2024", day, monthPlusOne)
        } else {
            null
        }

        if (selectedDay != null) {
            mSelectedDayViewModel.initialize(selectedDay)
        }

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.rvMatchCardsSelectedDay.apply {
            adapter = matchCardAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        with(mSelectedDayViewModel) {
            sortedMatches.observe(viewLifecycleOwner) { matches ->
                matches?.let {
                    val matchCardItems = matches.map { MatchCardBindingItem(it, teamRepository) }
                    matchCardAdapter.submitList(matchCardItems)
                }
            }
        }
    }
}