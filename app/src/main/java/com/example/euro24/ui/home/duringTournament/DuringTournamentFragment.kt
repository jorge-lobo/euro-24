package com.example.euro24.ui.home.duringTournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.databinding.FragmentDuringTournamentBinding
import com.example.euro24.ui.common.BaseFragment

/**
 * A simple [Fragment] subclass.
 * Use the [DuringTournamentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DuringTournamentFragment : BaseFragment() {

    private lateinit var binding: FragmentDuringTournamentBinding
    private val mDuringTournamentViewModel by lazy { ViewModelProvider(this)[DuringTournamentViewModel::class.java] }
    private val matchCardAdapter = MatchCardAdapter()

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

        setupRecyclerView()
        setupObservers()

        return binding.root
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
                    val matchCardItems = matches.map { MatchCardBindingItem(it) }
                    matchCardAdapter.submitList(matchCardItems)
                }
            }
        }
    }

}