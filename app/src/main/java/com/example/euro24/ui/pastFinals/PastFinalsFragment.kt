package com.example.euro24.ui.pastFinals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euro24.R
import com.example.euro24.databinding.FragmentPastFinalsBinding
import com.example.euro24.ui.common.BaseFragment

class PastFinalsFragment : BaseFragment() {

    private lateinit var binding: FragmentPastFinalsBinding
    private val mPastFinalsViewModel by lazy { ViewModelProvider(this)[PastFinalsViewModel::class.java] }
    private val pastFinalAdapter = PastFinalAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_past_finals,
            container,
            false
        )
        binding.viewModel = mPastFinalsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.rvPastFinals.apply {
            adapter = pastFinalAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        with(mPastFinalsViewModel) {
            sortedFinals.observe(viewLifecycleOwner) { pastFinals ->
                pastFinals?.let {
                    val pastFinalItems = pastFinals.map { PastFinalBindingItem(it) }
                    pastFinalAdapter.submitList(pastFinalItems)
                }
            }
        }
    }
}