package com.example.euro24.ui.main.internetConnection

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.euro24.R
import com.example.euro24.databinding.FragmentInternetConnectionBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.main.MainViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [InternetConnectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InternetConnectionFragment : BaseFragment() {

    private lateinit var binding: FragmentInternetConnectionBinding
    private val mMainViewModel: MainViewModel by viewModels()

    private lateinit var btnEnableWiFi: Button
    private lateinit var btnClose: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_internet_connection,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        setupViews()
        setupListeners()

        return binding.root
    }

    private fun setupViews() {
        btnClose = binding.dialogBackground.buttonCloseDialog
        btnEnableWiFi = binding.dialogBackground.buttonCtaDialog

        btnEnableWiFi.text = getString(R.string.dialog_button_enable_wi_fi)
    }

    private fun setupListeners() {
        btnClose.setOnClickListener {
            closeFragment()
        }

        btnEnableWiFi.setOnClickListener {
            openWiFiSettings()
            closeFragment()
        }
    }

    private fun openWiFiSettings() {
        val wifiIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
        startActivity(wifiIntent)
    }

    private fun closeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .remove(this)
            .commit()
        with(mMainViewModel) {
            getCurrentDate()
            checkDateCondition()
        }
    }

}