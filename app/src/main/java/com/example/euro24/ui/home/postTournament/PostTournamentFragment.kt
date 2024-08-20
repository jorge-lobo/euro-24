package com.example.euro24.ui.home.postTournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.databinding.FragmentPostTournamentBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.utils.ImagesResourceMap

class PostTournamentFragment : BaseFragment() {

    private lateinit var binding: FragmentPostTournamentBinding
    private lateinit var mPostTournamentViewModel: PostTournamentViewModel

    private val teamRepository: TeamRepository by lazy { TeamRepository(requireContext()) }
    private val defaultFlag = R.drawable.default_flag

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_post_tournament,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        mPostTournamentViewModel = ViewModelProvider(this)[PostTournamentViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
        with(mPostTournamentViewModel) {
            championId.observe(viewLifecycleOwner) { teamId ->
                updateChampionViews(teamId ?: 0)
            }
        }
    }

    private fun updateChampionViews(teamId: Int) {
        val champion = teamRepository.getTeamById(teamId)
        val championName = champion?.name ?: "Unknown"
        val championFlagResId =
            champion?.id?.let { ImagesResourceMap.flagResourceMapById[it] } ?: defaultFlag
        binding.apply {
            textChampionName.text = championName
            imageChampionFlag.setImageResource(championFlagResId)
        }
    }
}