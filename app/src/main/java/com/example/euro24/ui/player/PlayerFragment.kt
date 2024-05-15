package com.example.euro24.ui.player

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.euro24.R
import com.example.euro24.databinding.FragmentPlayerBinding
import com.example.euro24.ui.bottomNav.BottomNavActivity
import com.example.euro24.ui.common.BaseFragment

class PlayerFragment : BaseFragment() {

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var backButtonClickListener: OnBackButtonClickListener
    private val mPlayerViewModel by lazy { ViewModelProvider(this)[PlayerViewModel::class.java] }

    private val defaultFace = R.drawable.default_face
    private val defaultImage = R.drawable.default_image

    interface OnBackButtonClickListener {
        fun onBackButtonClicked(teamId: Int)
    }

    companion object {
        private const val ARG_PLAYER_ID = "player_id"
        private const val ARG_TEAM_ID = "team_id"

        fun newInstance(playerId: Int, teamId: Int): PlayerFragment {
            val fragment = PlayerFragment()
            val args = Bundle()
            args.putInt(ARG_PLAYER_ID, playerId)
            args.putInt(ARG_TEAM_ID, teamId)
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
            R.layout.fragment_player,
            container,
            false
        )

        binding.viewModel = mPlayerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playerId = arguments?.getInt(ARG_PLAYER_ID) ?: 0
        mPlayerViewModel.initialize(playerId)

        setupObservers()
        setupListeners()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBackButtonClickListener) {
            backButtonClickListener = context
        } else {
            throw RuntimeException("$context must implement OnBackButtonClickListener")
        }
    }

    private fun setupObservers() {
        with(mPlayerViewModel) {
            flagImageResourceId.observe(viewLifecycleOwner) { resourceId ->
                binding.imagePlayerBannerFlag.setImageResource(resourceId)
            }

            playerPhotoImageUrl.observe(viewLifecycleOwner) { imageUrl ->
                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(defaultFace)
                    .error(defaultFace)
                    .into(binding.imagePlayerBannerPhoto)
            }

            clubCrestImageUrl.observe(viewLifecycleOwner) { imageUrl ->
                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(defaultImage)
                    .error(defaultImage)
                    .into(binding.imageClubCrest)
            }
        }
    }

    private fun setupListeners() {
        val activity = activity
        if (activity is BottomNavActivity) {
            activity.findViewById<ImageButton>(R.id.button_back_icon)?.setOnClickListener {
                val teamId = arguments?.getInt(ARG_TEAM_ID) ?: 0
                onBackButtonClicked(teamId)
            }
        }
    }

    private fun onBackButtonClicked(teamId: Int) {
        backButtonClickListener.onBackButtonClicked(teamId)
    }
}