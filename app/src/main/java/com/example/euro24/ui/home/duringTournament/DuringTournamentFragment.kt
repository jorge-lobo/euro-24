package com.example.euro24.ui.home.duringTournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.databinding.FragmentDuringTournamentBinding
import com.example.euro24.ui.common.BaseFragment

class DuringTournamentFragment : BaseFragment() {

    private lateinit var binding: FragmentDuringTournamentBinding
    private val mDuringTournamentViewModel by lazy { ViewModelProvider(this)[DuringTournamentViewModel::class.java] }
    private lateinit var teamRepository: TeamRepository
    private lateinit var matchCardAdapter: MatchCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_during_tournament,
            container,
            false
        )

        binding.viewModel = mDuringTournamentViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamRepository = TeamRepository(requireContext())
        matchCardAdapter = MatchCardAdapter(teamRepository)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.rvMatchCards.apply {
            adapter = matchCardAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        with(mDuringTournamentViewModel) {
            sortedMatches.observe(viewLifecycleOwner) { matches ->
                matches?.let {
                    val matchCardItems = matches.map { MatchCardBindingItem(it, teamRepository) }
                    matchCardAdapter.submitList(matchCardItems)
                }
            }
        }
    }
}