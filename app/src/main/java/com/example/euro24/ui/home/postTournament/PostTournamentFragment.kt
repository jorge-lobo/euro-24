package com.example.euro24.ui.home.postTournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentPostTournamentBinding
import com.example.euro24.ui.common.BaseFragment

class PostTournamentFragment : BaseFragment() {

    private lateinit var binding: FragmentPostTournamentBinding
    private lateinit var mPostTournamentViewModel: PostTournamentViewModel

    private lateinit var textChampionName: TextView
    private lateinit var imageChampionFlag: ImageView

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

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        textChampionName = binding.textChampionName
        imageChampionFlag = binding.imageChampionFlag
    }

    private fun setupObservers() {
        with(mPostTournamentViewModel) {
            championName.observe(viewLifecycleOwner) { championName ->
                textChampionName.text = championName
            }
        }
    }
}