package com.example.euro24.ui.teams.teamDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentTeamDetailBinding
import com.example.euro24.ui.common.BaseFragment

class TeamDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentTeamDetailBinding
    private val mTeamDetailViewModel by lazy { ViewModelProvider(this)[TeamDetailViewModel::class.java] }

    companion object {
        private const val ARG_TEAM_ID = "team_id"

        fun newInstance(teamId: Int): TeamDetailFragment {
            val fragment = TeamDetailFragment()
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
            R.layout.fragment_team_detail,
            container,
            false
        )
        binding.viewModel = mTeamDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        openFragment(TeamInfoFragment())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val teamId = arguments?.getInt(ARG_TEAM_ID) ?: 0
        mTeamDetailViewModel.initialize(teamId)

        setupViews()
        setupObservers()
        setupListeners()
    }

    private fun setupViews() {
        activity?.findViewById<View>(R.id.button_back_icon)?.visibility = View.VISIBLE
    }

    private fun setupObservers() {
        with(mTeamDetailViewModel) {
            teamFlagResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.imageTeamFlagHeader.setImageResource(resourceId)
            }

        }
    }

    private fun setupListeners() {
        binding.buttonsContainer.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_team_info -> openFragment(TeamInfoFragment())
                R.id.rb_team_matches -> openFragment(TeamMatchesFragment())
                R.id.rb_team_squad -> openFragment(TeamSquadFragment())
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val teamId = arguments?.getInt(ARG_TEAM_ID) ?: 0
        fragment.arguments = Bundle().apply {
            putInt(ARG_TEAM_ID, teamId)
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.team_detail_fragment_container, fragment)
            .commit()
    }

}