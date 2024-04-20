package com.example.euro24.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.euro24.R
import com.example.euro24.databinding.ActivityMainBinding
import com.example.euro24.ui.main.internetConnection.InternetConnectionFragment
import com.example.euro24.utils.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mMainViewModel: MainViewModel by viewModels()
    private var isInitialConnectionChecked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mMainViewModel.checkInternetConnectivity()

        setupObservers()

    }

    private fun setupObservers() {
        with(mMainViewModel) {
            internetConnection.observe(this@MainActivity) { isConnected ->
                if (!isInitialConnectionChecked) {
                    isInitialConnectionChecked = true
                    if (isConnected) {
                        Utils.showToast(this@MainActivity, "Internet connectivity is OK!")
                    } else {
                        Utils.showToast(
                            this@MainActivity,
                            "Warning!\nThere's no Internet connection!"
                        )
                        openInternetConnectionDialog()
                    }
                }
            }
        }
    }

    private fun openInternetConnectionDialog() {
        val internetConnectionDialogFragment = InternetConnectionFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, internetConnectionDialogFragment)
            .commit()
    }
}