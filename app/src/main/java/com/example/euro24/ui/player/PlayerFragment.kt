package com.example.euro24.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.euro24.R
import com.example.euro24.ui.common.BaseFragment

class PlayerFragment : BaseFragment() {


    companion object {
        private const val ARG_PLAYER_ID = "player_id"

        fun newInstance(playerId: Int): PlayerFragment {
            val fragment = PlayerFragment()
            val args = Bundle()
            args.putInt(ARG_PLAYER_ID, playerId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

}