package com.example.euro24.ui.teams.teamDetail.teamMatches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.databinding.FragmentTeamMatchesBinding
import com.example.euro24.ui.common.BaseFragment

class TeamMatchesFragment : BaseFragment() {

    private lateinit var binding: FragmentTeamMatchesBinding
    private val mTeamMatchesViewModel by lazy { ViewModelProvider(this)[TeamMatchesViewModel::class.java] }
    private var teamMatchesAdapter = TeamMatchesAdapter()

    companion object {
        private const val ARG_TEAM_ID = "team_id"

        fun newInstance(teamId: Int): TeamMatchesFragment {
            val fragment = TeamMatchesFragment()
            val args = Bundle()
            args.putInt(ARG_TEAM_ID, teamId)
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
            R.layout.fragment_team_matches,
            container,
            false
        )

        binding.viewModel = mTeamMatchesViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val teamId = arguments?.getInt(TeamMatchesFragment.ARG_TEAM_ID) ?: 0
        mTeamMatchesViewModel.initialize(teamId)

        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.rvTeamMatches.apply {
            adapter = teamMatchesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        with(mTeamMatchesViewModel) {
            sortedMatches.observe(viewLifecycleOwner) { matches ->
                matches?.let {
                    val matchCardNarrowItems = matches.map { TeamMatchesBindingItem(it) }
                    teamMatchesAdapter.submitList(matchCardNarrowItems)
                }
            }
        }
    }

}