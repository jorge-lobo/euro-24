package com.example.euro24.ui.teams.teamDetail.teamSquad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.databinding.FragmentTeamSquadBinding
import com.example.euro24.ui.common.BaseFragment
import java.util.Stack

class TeamSquadFragment : BaseFragment() {

    private lateinit var binding: FragmentTeamSquadBinding
    private val mTeamSquadViewModel by lazy { ViewModelProvider(this)[TeamSquadViewModel::class.java] }
    private val fragmentStack: Stack<Fragment> = Stack()

    private lateinit var forwardsAdapter: TeamSquadAdapter
    private lateinit var midfieldersAdapter: TeamSquadAdapter
    private lateinit var defendersAdapter: TeamSquadAdapter
    private lateinit var goalkeepersAdapter: TeamSquadAdapter
    private lateinit var headManagerAdapter: TeamSquadAdapter

    companion object {
        private const val ARG_TEAM_ID = "team_id"

        fun newInstance(teamId: Int): TeamSquadFragment {
            val fragment = TeamSquadFragment()
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
            R.layout.fragment_team_squad,
            container,
            false
        )

        binding.viewModel = mTeamSquadViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val teamId = arguments?.getInt(ARG_TEAM_ID) ?: 0
        mTeamSquadViewModel.initialize(teamId)

        setupProgressBar()
        setupObservers()
        initializeAdapters()
        setupRecyclerViews()
    }

    private fun setupProgressBar() {
        binding.loadingSquad.visibility = View.VISIBLE
    }

    private fun setupRecyclerViews() {
        with(binding) {
            rvSquadForwards.apply {
                adapter = forwardsAdapter
                layoutManager = LinearLayoutManager(context)
            }

            rvSquadMidfielders.apply {
                adapter = midfieldersAdapter
                layoutManager = LinearLayoutManager(context)
            }

            rvSquadDefenders.apply {
                adapter = defendersAdapter
                layoutManager = LinearLayoutManager(context)
            }

            rvSquadGoalkeepers.apply {
                adapter = goalkeepersAdapter
                layoutManager = LinearLayoutManager(context)
            }

            rvSquadHeadManager.apply {
                adapter = headManagerAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun setupObservers() {
        with(mTeamSquadViewModel) {
            forwards.observe(viewLifecycleOwner) { players ->
                players?.let {
                    val playerCardItems = players.map { TeamSquadBindingItem(it) }
                    forwardsAdapter.submitList(playerCardItems)
                }
            }

            midfielders.observe(viewLifecycleOwner) { players ->
                players?.let {
                    val playerCardItems = players.map { TeamSquadBindingItem(it) }
                    midfieldersAdapter.submitList(playerCardItems)
                }
            }

            defenders.observe(viewLifecycleOwner) { players ->
                players?.let {
                    val playerCardItems = players.map { TeamSquadBindingItem(it) }
                    defendersAdapter.submitList(playerCardItems)
                }
            }

            goalkeepers.observe(viewLifecycleOwner) { players ->
                players?.let {
                    val playerCardItems = players.map { TeamSquadBindingItem(it) }
                    goalkeepersAdapter.submitList(playerCardItems)
                }
            }

            headManager.observe(viewLifecycleOwner) { headManager ->
                headManager?.let {
                    val managerItem = TeamSquadBindingItem(manager = headManager)
                    val managerList = listOf(managerItem)
                    headManagerAdapter.submitList(managerList)
                }
            }

            loadComplete.observe(viewLifecycleOwner) { isComplete ->
                if (isComplete == true) {
                    showUI()
                }
            }
        }
    }

    private fun initializeAdapters() {
        val teamId = requireArguments().getInt(ARG_TEAM_ID)

        forwardsAdapter = TeamSquadAdapter(fragmentStack, teamId)
        midfieldersAdapter = TeamSquadAdapter(fragmentStack, teamId)
        defendersAdapter = TeamSquadAdapter(fragmentStack, teamId)
        goalkeepersAdapter = TeamSquadAdapter(fragmentStack, teamId)
        headManagerAdapter = TeamSquadAdapter(fragmentStack, teamId)
    }

    private fun showUI() {
        with(binding) {
            squadContainer.visibility = View.VISIBLE
            loadingSquad.visibility = View.GONE
        }
    }
}