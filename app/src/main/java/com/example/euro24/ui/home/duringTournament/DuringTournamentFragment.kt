package com.example.euro24.ui.home.duringTournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.databinding.FragmentDuringTournamentBinding
import com.example.euro24.ui.common.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DuringTournamentFragment : BaseFragment() {

    private lateinit var binding: FragmentDuringTournamentBinding
    private val mDuringTournamentViewModel by lazy { ViewModelProvider(this)[DuringTournamentViewModel::class.java] }
    private lateinit var teamRepository: TeamRepository
    private lateinit var matchCardAdapter: MatchCardAdapter

    companion object {
        private const val ARG_MATCH_ID = "match_id"

        fun newInstance(matchId: Int): DuringTournamentFragment {
            val fragment = DuringTournamentFragment()
            val args = Bundle()
            args.putInt(ARG_MATCH_ID, matchId)
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
        matchCardAdapter = MatchCardAdapter(teamRepository, this)

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

    fun showMatchEditorFragmentContainer() {
        with(requireActivity()) {
            findViewById<FrameLayout>(R.id.fragment_container).visibility = View.INVISIBLE
            findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE
            findViewById<ImageView>(R.id.image_background).visibility = View.INVISIBLE
            findViewById<FrameLayout>(R.id.match_editor_fragment_container).visibility =
                View.VISIBLE
        }
    }
}