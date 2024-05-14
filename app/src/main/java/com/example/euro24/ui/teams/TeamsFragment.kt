package com.example.euro24.ui.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.data.teams.Team
import com.example.euro24.databinding.FragmentTeamsBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.teams.teamDetail.TeamDetailFragment

class TeamsFragment : BaseFragment(), TeamGridAdapter.OnItemClickListener {

    private lateinit var binding: FragmentTeamsBinding
    private val mTeamsViewModel by lazy { ViewModelProvider(this) [TeamsViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_teams,
            container,
            false
        )
        binding.viewModel = mTeamsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTeamsViewModel.loadTeams()
        setupObservers()
    }

    override fun onItemClick(team: Team) {
        val fragment = TeamDetailFragment.newInstance(team.id ?: 0)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setupObservers() {
        mTeamsViewModel.teams.observe(viewLifecycleOwner) { teams ->
            teams?.let {
                val sortedTeamList = it.sortedBy { team -> team.name }
                val adapter = TeamGridAdapter(requireContext(), sortedTeamList, this)
                binding.gridTeams.adapter = adapter
            }
        }
    }

}