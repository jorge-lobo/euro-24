package com.example.euro24.ui.home

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentHomeBinding
import com.example.euro24.ui.bottomNav.BottomNavActivity
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.home.duringTournament.DuringTournamentFragment
import com.example.euro24.ui.home.internetConnection.InternetConnectionFragment
import com.example.euro24.ui.home.postTournament.PostTournamentFragment
import com.example.euro24.ui.home.preTournament.PreTournamentFragment

class HomeFragment : BaseFragment(), InternetConnectionFragment.InternetConnectionFragmentListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mHomeViewModel: HomeViewModel
    private var isInitialConnectionChecked = false

    private lateinit var textCurrentDate: TextView

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            mHomeViewModel.setInternetConnection(true)
        }

        override fun onLost(network: Network) {
            mHomeViewModel.setInternetConnection(false)
        }
    }

    interface InternetConnectionFragmentListener {
        fun onInternetConnectionFragmentOpened()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        mHomeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
        mHomeViewModel.checkInternetConnectivity()
    }

    private fun setupViews() {
        textCurrentDate = binding.actualDateText.apply {
            text = mHomeViewModel.getCurrentDate()
        }
    }

    private fun setupConnectivity() {
        val connectivityManager = requireActivity().getSystemService(ConnectivityManager::class.java)
        connectivityManager?.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            networkCallback
        )
        mHomeViewModel.isNetworkCallbackRegistered = true
    }

    override fun onInternetConnectionFragmentClosed() {
        with(mHomeViewModel) {
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
        if (mHomeViewModel.isNetworkCallbackRegistered) {
            val connectivityManager = requireActivity().getSystemService(ConnectivityManager::class.java)
            connectivityManager?.unregisterNetworkCallback(networkCallback)
            mHomeViewModel.isNetworkCallbackRegistered = false
        }
    }

    private fun setupObservers() {
        with(mHomeViewModel) {
            internetConnection.observe(viewLifecycleOwner) { isConnected ->
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

            dateCondition.observe(viewLifecycleOwner) { condition ->
                when (condition!!) {
                    HomeViewModel.DateCondition.PRE_TOURNAMENT -> openFragment(PreTournamentFragment())
                    HomeViewModel.DateCondition.DURING_TOURNAMENT -> openFragment(DuringTournamentFragment())
                    HomeViewModel.DateCondition.POST_TOURNAMENT -> openFragment(PostTournamentFragment())
                }
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.home_fragment_container, fragment)
            .commit()
        when (fragment) {
            is InternetConnectionFragment -> {
                fragment.setInternetConnectionListener(this@HomeFragment)
                (requireActivity() as? BottomNavActivity)?.onInternetConnectionFragmentOpened()
            }
        }
    }
}