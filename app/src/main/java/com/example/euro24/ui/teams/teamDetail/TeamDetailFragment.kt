package com.example.euro24.ui.teams.teamDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.euro24.R
import com.example.euro24.databinding.FragmentTeamDetailBinding
import com.example.euro24.ui.common.BaseFragment

class TeamDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentTeamDetailBinding


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
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_detail, container, false)
    }

}