package com.example.euro24.ui.bottomNav

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.euro24.R
import com.example.euro24.databinding.ActivityBottomNavBinding
import com.example.euro24.ui.pastFinals.PastFinalsFragment
import com.example.euro24.ui.home.HomeFragment
import com.example.euro24.ui.home.internetConnection.InternetConnectionFragment
import com.example.euro24.ui.hostCities.HostCitiesFragment
import com.example.euro24.ui.hostCities.venueDetail.VenueDetailFragment
import com.example.euro24.ui.matches.MatchesFragment
import com.example.euro24.ui.matches.calendar.CalendarFragment
import com.example.euro24.ui.player.PlayerFragment
import com.example.euro24.ui.teams.TeamsFragment
import com.example.euro24.ui.teams.teamDetail.TeamDetailFragment
import java.util.Stack

class BottomNavActivity : AppCompatActivity(),
    HomeFragment.InternetConnectionFragmentListener,
    InternetConnectionFragment.InternetConnectionFragmentListener,
    PlayerFragment.OnBackButtonClickListener {

    private lateinit var binding: ActivityBottomNavBinding
    private val fragmentStack: Stack<Fragment> = Stack()
    private var isSelectingItemManually = false

    private val fragmentHome = R.id.nav_home
    private val fragmentMatches = R.id.nav_matches
    private val fragmentTeams = R.id.nav_teams
    private val fragmentCities = R.id.nav_cities
    private val fragmentPastFinals = R.id.nav_historic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openFragment(HomeFragment())
        setupListeners()
        setupBackButton()
    }

    override fun onInternetConnectionFragmentOpened() {
        binding.bottomNav.visibility = View.INVISIBLE
    }

    override fun onInternetConnectionFragmentClosed() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    override fun onBackButtonClicked(teamId: Int) {
        val teamDetailFragment = TeamDetailFragment.newInstance(teamId, R.id.rb_team_squad)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, teamDetailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupBackButton() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedAction()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setupListeners() {
        with(binding) {
            bottomNav.setOnItemSelectedListener { menuItem ->
                isSelectingItemManually = true
                when (menuItem.itemId) {
                    fragmentHome -> openFragment(HomeFragment())
                    fragmentMatches -> openFragment(MatchesFragment())
                    fragmentTeams -> openFragment(TeamsFragment())
                    fragmentCities -> openFragment(HostCitiesFragment())
                    fragmentPastFinals -> openFragment(PastFinalsFragment())
                }
                isSelectingItemManually = false
                true
            }

            buttonBack.buttonBackIcon.setOnClickListener {
                onBackPressedAction()
            }

            buttonCalendar.buttonCalendar.setOnClickListener {
                openFragment(CalendarFragment())
            }
        }
    }

    private fun onBackPressedAction() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (binding.venueDetailFragmentContainer.visibility == View.VISIBLE) {
            openFragment(HostCitiesFragment())
            hideVenueDetailFragmentContainer()
            return
        } else if (currentFragment is TeamDetailFragment) {
            openFragment(TeamsFragment())
        } else {
            openFragment(HomeFragment())
        }
    }

    fun openFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .apply {
                if (addToBackStack) {
                    addToBackStack(null)
                    fragmentStack.push(fragment)
                }
            }
            .commit()

        hideVenueDetailFragmentContainer()
        updateUIVisibility(fragment)

        if (!isSelectingItemManually) {
            updateBottomNavState(fragment)
        }
    }

    private fun showFragmentContainer() {
        binding.apply {
            fragmentContainer.visibility = View.VISIBLE
        }
    }

    private fun hideVenueDetailFragmentContainer() {
        showFragmentContainer()
        binding.apply {
            venueDetailFragmentContainer.visibility = View.INVISIBLE

            wordMark.headerLogo.setColorFilter(
                ContextCompat.getColor(
                    this@BottomNavActivity,
                    R.color.common_header_logo
                )
            )
        }
    }

    fun hideMatchEditorFragmentContainer() {
        showFragmentContainer()
        binding.apply {
            matchEditorFragmentContainer.visibility = View.INVISIBLE
            bottomNav.visibility = View.VISIBLE
        }
    }

    private fun updateBottomNavState(fragment: Fragment) {
        binding.bottomNav.selectedItemId = when (fragment) {
            is HomeFragment -> fragmentHome
            is MatchesFragment -> fragmentMatches
            is TeamsFragment -> fragmentTeams
            is HostCitiesFragment -> fragmentCities
            is PastFinalsFragment -> fragmentPastFinals
            else -> fragmentHome
        }
    }

    private fun updateUIVisibility(fragment: Fragment) {
        binding.apply {
            imageBackground.visibility =
                if (fragment is HomeFragment) View.VISIBLE else View.INVISIBLE
            buttonBack.buttonBackIcon.visibility =
                if (fragment is VenueDetailFragment) View.VISIBLE else View.INVISIBLE
            buttonCalendar.buttonCalendar.visibility =
                if (fragment is MatchesFragment) View.VISIBLE else View.INVISIBLE
        }
    }
}