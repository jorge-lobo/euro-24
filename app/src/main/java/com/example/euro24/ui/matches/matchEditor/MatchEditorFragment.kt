package com.example.euro24.ui.matches.matchEditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentMatchEditorBinding
import com.example.euro24.ui.common.BaseFragment

class MatchEditorFragment : BaseFragment() {

    private lateinit var binding: FragmentMatchEditorBinding
    private val matchEditorViewModel by lazy { ViewModelProvider(this)[MatchEditorViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_match_editor,
            container,
            false
        )
        binding.viewModel = matchEditorViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

}