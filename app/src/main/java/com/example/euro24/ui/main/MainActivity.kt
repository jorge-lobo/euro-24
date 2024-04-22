package com.example.euro24.ui.main

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.euro24.R
import com.example.euro24.databinding.ActivityMainBinding
import com.example.euro24.ui.main.internetConnection.InternetConnectionFragment
import com.example.euro24.ui.main.postTournament.PostTournamentFragment
import com.example.euro24.ui.main.preTournament.PreTournamentFragment
import com.example.euro24.utils.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mMainViewModel: MainViewModel by viewModels()
    private var isInitialConnectionChecked = false

    private lateinit var textCurrentDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mMainViewModel.checkInternetConnectivity()

        setupObservers()
        setupViews()

    }

    private fun setupObservers() {
        with(mMainViewModel) {
            internetConnection.observe(this@MainActivity) { isConnected ->
                if (!isInitialConnectionChecked) {
                    isInitialConnectionChecked = true
                    if (isConnected) {
                        /*Utils.showToast(this@MainActivity, "Internet connectivity is OK!")*/
                        getCurrentDate()
                        checkDateCondition()
                    } else {
                        /*Utils.showToast(
                            this@MainActivity,
                            "Warning!\nThere's no Internet connection!"
                        )*/
                        openInternetConnectionDialog()
                    }
                }
            }

            dateCondition.observe(this@MainActivity) { condition ->
                when (condition) {
                    MainViewModel.DateCondition.PRE_TOURNAMENT -> openPreTournamentFragment()
                    MainViewModel.DateCondition.DURING_TOURNAMENT -> openDuringTournamentFragment()
                    MainViewModel.DateCondition.POST_TOURNAMENT -> openPostTournamentFragment()
                }
            }
        }
    }

    private fun setupViews() {
        textCurrentDate = binding.actualDateText
        textCurrentDate.text = mMainViewModel.getCurrentDate()
    }

    private fun openInternetConnectionDialog() {
        val internetConnectionFragment = InternetConnectionFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.internet_connection_fragment_container, internetConnectionFragment)
            .commit()
    }

    private fun openPreTournamentFragment() {
        val preTournamentFragment = PreTournamentFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.tournament_fragment_container, preTournamentFragment)
            .commit()
        Utils.showToast(this, "PreTournamentFragment")
    }

    private fun openDuringTournamentFragment() {
        val duringTournamentFragment = PreTournamentFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.tournament_fragment_container, duringTournamentFragment)
            .commit()
        Utils.showToast(this, "DuringTournamentFragment")
    }

    private fun openPostTournamentFragment() {
        val postTournamentFragment = PostTournamentFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.tournament_fragment_container, postTournamentFragment)
            .commit()
        Utils.showToast(this, "PostTournamentFragment")
    }
}