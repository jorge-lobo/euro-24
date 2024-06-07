package com.example.euro24.ui.matches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentMatchesBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.matches.matchesGroupStage.MatchesGroupStageFragment
import com.example.euro24.ui.matches.matchesKnockout.MatchesKnockoutFragment

class MatchesFragment : BaseFragment() {

    private lateinit var binding: FragmentMatchesBinding
    private val mMatchesViewModel by lazy { ViewModelProvider(this)[MatchesViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_matches,
            container,
            false
        )
        binding.viewModel = mMatchesViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
        mMatchesViewModel.checkDateCondition()
    }

    private fun setupObservers() {
        with(mMatchesViewModel) {
            dateCondition.observe(viewLifecycleOwner) { condition ->
                when (condition!!) {
                    MatchesViewModel.DateCondition.GROUP_STAGE -> openFragment(
                        MatchesGroupStageFragment()
                    )

                    MatchesViewModel.DateCondition.KNOCKOUT -> openFragment(MatchesKnockoutFragment())
                }
            }
        }
    }

    private fun setupListeners() {
        binding.buttonsContainer.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_matches_group_stage -> openFragment(MatchesGroupStageFragment())
                R.id.rb_matches_knockout -> openFragment(MatchesKnockoutFragment())
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        if (childFragmentManager.findFragmentById(R.id.matches_stage_container) != fragment) {
            childFragmentManager.beginTransaction()
                .replace(R.id.matches_stage_container, fragment)
                .commit()

            if (fragment is MatchesKnockoutFragment) {
                with(binding) {
                    rbMatchesKnockout.isChecked = true
                    rbMatchesGroupStage.isChecked = false
                }
            } else {
                with(binding) {
                    rbMatchesKnockout.isChecked = false
                    rbMatchesGroupStage.isChecked = true
                }
            }
        }
    }
}