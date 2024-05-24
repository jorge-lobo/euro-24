package com.example.euro24.ui.matches.matchesKnockout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.data.teams.TeamRepository
import com.example.euro24.databinding.FragmentMatchesKnockoutBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.matches.MatchNarrowCardAdapter

class MatchesKnockoutFragment : BaseFragment() {

    private lateinit var binding: FragmentMatchesKnockoutBinding
    private val mMatchesKnockoutViewModel by lazy { ViewModelProvider(this)[MatchesKnockoutViewModel::class.java] }
    private lateinit var teamRepository: TeamRepository
    private lateinit var roundMatchesAdapter: MatchNarrowCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_matches_knockout,
            container,
            false
        )
        binding.viewModel = mMatchesKnockoutViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamRepository = TeamRepository(requireContext())
        roundMatchesAdapter = MatchNarrowCardAdapter(teamRepository)

        setupRecyclerView()
        setupObservers()
        setupListeners()
        mMatchesKnockoutViewModel.checkDateCondition()
    }

    private fun setupRecyclerView() {
        binding.rvKnockoutMatches.apply {
            adapter = roundMatchesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        with(mMatchesKnockoutViewModel) {
            matchesInSelectedRound.observe(viewLifecycleOwner) { matches ->
                roundMatchesAdapter.submitList(matches)
            }

            dateCondition.observe(viewLifecycleOwner) { condition ->
                updateUI(condition)
            }
        }
    }

    private fun setupListeners() {
        binding.buttonsContainer.setOnCheckedChangeListener { _, checkedId ->
            val newCondition = when (checkedId) {
                R.id.rb_matches_round_16 -> MatchesKnockoutViewModel.DateCondition.ROUND_OF_16
                R.id.rb_matches_quarter_finals -> MatchesKnockoutViewModel.DateCondition.QUARTER_FINALS
                R.id.rb_matches_semi_finals -> MatchesKnockoutViewModel.DateCondition.SEMI_FINALS
                R.id.rb_matches_final -> MatchesKnockoutViewModel.DateCondition.FINAL
                else -> return@setOnCheckedChangeListener
            }
            mMatchesKnockoutViewModel.filterMatchesByPhase(newCondition)
        }
    }

    private fun updateUI(condition: MatchesKnockoutViewModel.DateCondition) {
        with(binding) {
            buttonsContainer.check(getRadioButtonId(condition))
            textRoundTitle.setText(getTitleResId(condition))
        }
    }

    private fun getRadioButtonId(condition: MatchesKnockoutViewModel.DateCondition): Int {
        return when (condition) {
            MatchesKnockoutViewModel.DateCondition.ROUND_OF_16 -> R.id.rb_matches_round_16
            MatchesKnockoutViewModel.DateCondition.QUARTER_FINALS -> R.id.rb_matches_quarter_finals
            MatchesKnockoutViewModel.DateCondition.SEMI_FINALS -> R.id.rb_matches_semi_finals
            MatchesKnockoutViewModel.DateCondition.FINAL -> R.id.rb_matches_final
        }
    }

    private fun getTitleResId(condition: MatchesKnockoutViewModel.DateCondition): Int {
        return when (condition) {
            MatchesKnockoutViewModel.DateCondition.ROUND_OF_16 -> R.string.matches_round_of_16
            MatchesKnockoutViewModel.DateCondition.QUARTER_FINALS -> R.string.matches_quarter_finals
            MatchesKnockoutViewModel.DateCondition.SEMI_FINALS -> R.string.matches_semi_finals
            MatchesKnockoutViewModel.DateCondition.FINAL -> R.string.matches_final
        }
    }
}