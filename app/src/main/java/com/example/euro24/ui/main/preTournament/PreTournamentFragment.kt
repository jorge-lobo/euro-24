package com.example.euro24.ui.main.preTournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentPreTournamentBinding
import com.example.euro24.ui.common.BaseFragment

/**
 * A simple [Fragment] subclass.
 * Use the [PreTournamentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreTournamentFragment : BaseFragment() {

    private lateinit var binding: FragmentPreTournamentBinding
    private lateinit var mPreTournamentViewModel: PreTournamentViewModel

    private lateinit var textNumberDays: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pre_tournament,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        mPreTournamentViewModel = ViewModelProvider(this)[PreTournamentViewModel::class.java]

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        textNumberDays = binding.textCountingNumberDays

        val daysToTournament = mPreTournamentViewModel.countDaysToTournament()
        textNumberDays.text = daysToTournament.toString()
    }

}