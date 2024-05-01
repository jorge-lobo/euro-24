package com.example.euro24.ui.main

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.euro24.R
import com.example.euro24.databinding.ActivityMainBinding
import com.example.euro24.ui.bottomNav.BottomNavFragment
import com.example.euro24.ui.main.duringTournament.DuringTournamentFragment
import com.example.euro24.ui.main.internetConnection.InternetConnectionFragment
import com.example.euro24.ui.main.postTournament.PostTournamentFragment
import com.example.euro24.ui.main.preTournament.PreTournamentFragment
import com.example.euro24.utils.Utils

class MainActivity : AppCompatActivity(), InternetConnectionFragment.InternetConnectionListener {

    private lateinit var binding: ActivityMainBinding
    private val mMainViewModel: MainViewModel by viewModels()
    private var isInitialConnectionChecked = false

    private lateinit var textCurrentDate: TextView

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            mMainViewModel.setInternetConnection(true)
        }

        override fun onLost(network: Network) {
            mMainViewModel.setInternetConnection(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
        mMainViewModel.checkInternetConnectivity()
        setupBottomNav()

    }

    private fun setupViews() {
        textCurrentDate = binding.actualDateText.apply {
            text = mMainViewModel.getCurrentDate()
        }
    }

    private fun setupConnectivity() {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager?.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            networkCallback
        )
        mMainViewModel.isNetworkCallbackRegistered = true
    }

    override fun onInternetConnectionFragmentClosed() {
        binding.bottomNavFragment.visibility = View.VISIBLE
        with(mMainViewModel) {
            getCurrentDate()
            checkDateCondition()
        }
    }

    override fun onStart() {
        super.onStart()
        setupConnectivity()
    }

    override fun onStop() {
        super.onStop()
        unregisterNetworkCallback()
    }

    private fun unregisterNetworkCallback() {
        if (mMainViewModel.isNetworkCallbackRegistered) {
            val connectivityManager = getSystemService(ConnectivityManager::class.java)
            connectivityManager?.unregisterNetworkCallback(networkCallback)
            mMainViewModel.isNetworkCallbackRegistered = false
        }
    }

    private fun setupObservers() {
        with(mMainViewModel) {
            internetConnection.observe(this@MainActivity) { isConnected ->
                if (!isInitialConnectionChecked) {
                    isInitialConnectionChecked = true
                    if (isConnected) {
                        getCurrentDate()
                        checkDateCondition()
                    } else {
                        openFragment(InternetConnectionFragment())
                    }
                }
            }

            dateCondition.observe(this@MainActivity) { condition ->
                when (condition!!) {
                    MainViewModel.DateCondition.PRE_TOURNAMENT -> openFragment(PreTournamentFragment())
                    MainViewModel.DateCondition.DURING_TOURNAMENT -> openFragment(DuringTournamentFragment())
                    MainViewModel.DateCondition.POST_TOURNAMENT -> openFragment(PostTournamentFragment())
                }
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.tournament_fragment_container, fragment)
            .commit()
        when (fragment) {
            is InternetConnectionFragment -> {
                (fragment as? InternetConnectionFragment)?.setInternetConnectionListener(this)
                binding.bottomNavFragment.visibility = View.INVISIBLE
            }
            is PreTournamentFragment -> Utils.showToast(this, "PreTournamentFragment")
            is DuringTournamentFragment -> Utils.showToast(this, "DuringTournamentFragment")
            is PostTournamentFragment -> Utils.showToast(this, "PostTournamentFragment")
        }
    }

    private fun setupBottomNav() {
        val bottomNavFragment = BottomNavFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.bottom_nav_fragment, bottomNavFragment)
            .commit()
    }
}