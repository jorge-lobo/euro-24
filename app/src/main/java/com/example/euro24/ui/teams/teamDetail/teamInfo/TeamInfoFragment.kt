package com.example.euro24.ui.teams.teamDetail.teamInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentTeamInfoBinding
import com.example.euro24.ui.common.BaseFragment

class TeamInfoFragment : BaseFragment() {

    private lateinit var binding: FragmentTeamInfoBinding
    private val mTeamInfoViewModel by lazy { ViewModelProvider(this)[TeamInfoViewModel::class.java] }

    companion object {
        private const val ARG_TEAM_ID = "team_id"

        fun newInstance(teamId: Int): TeamInfoFragment {
            val fragment = TeamInfoFragment()
            val args = Bundle()
            args.putInt(ARG_TEAM_ID, teamId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_team_info,
            container,
            false
        )

        binding.viewModel = mTeamInfoViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val teamId = arguments?.getInt(ARG_TEAM_ID) ?: 0
        mTeamInfoViewModel.initialize(teamId)

        setupObservers()
    }

    private fun setupObservers(){
        with(mTeamInfoViewModel) {
            teamPhotoResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.imageTeamPhoto.setImageResource(resourceId)
            }
            teamCrestResourceId.observe(viewLifecycleOwner) {resourceId ->
                binding.imageTeamCrest.setImageResource(resourceId)
            }
        }
    }

}