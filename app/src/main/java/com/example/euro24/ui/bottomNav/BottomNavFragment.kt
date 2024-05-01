package com.example.euro24.ui.bottomNav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.euro24.R
import com.example.euro24.databinding.FragmentBottomNavBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.history.HistoryActivity
import com.example.euro24.ui.hostCities.HostCitiesActivity
import com.example.euro24.ui.main.MainActivity
import com.example.euro24.ui.matches.MatchesActivity
import com.example.euro24.ui.teams.TeamsActivity
import com.example.euro24.utils.Utils

class BottomNavFragment : BaseFragment() {

    private lateinit var binding: FragmentBottomNavBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottom_nav,
            container,
            false
        )

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> openMainActivity()
                R.id.nav_matches -> openMatchesActivity()
                R.id.nav_teams -> openTeamsActivity()
                R.id.nav_cities -> openHostCitiesActivity()
                R.id.nav_historic -> openHistoryActivity()
            }
            true
        }

        return binding.root
    }

    private fun openMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        Utils.showToast(requireContext(), "MainActivity clicked")
    }

    private fun openMatchesActivity() {
        val intent = Intent(requireContext(), MatchesActivity::class.java)
        startActivity(intent)
        Utils.showToast(requireContext(), "MatchesActivity clicked")
    }

    private fun openTeamsActivity() {
        val intent = Intent(requireContext(), TeamsActivity::class.java)
        startActivity(intent)
        Utils.showToast(requireContext(), "TeamsActivity clicked")
    }

    private fun openHostCitiesActivity() {
        val intent = Intent(requireContext(), HostCitiesActivity::class.java)
        startActivity(intent)
        Utils.showToast(requireContext(), "HostCitiesActivity clicked")
    }

    private fun openHistoryActivity() {
        val intent = Intent(requireContext(), HistoryActivity::class.java)
        startActivity(intent)
        Utils.showToast(requireContext(), "HistoryActivity clicked")
    }
}