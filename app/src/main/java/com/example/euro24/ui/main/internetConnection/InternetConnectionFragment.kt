package com.example.euro24.ui.main.internetConnection

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import com.example.euro24.R
import com.example.euro24.databinding.FragmentInternetConnectionBinding
import com.example.euro24.ui.common.BaseFragment

class InternetConnectionFragment : BaseFragment() {

    private lateinit var binding: FragmentInternetConnectionBinding
    private lateinit var btnEnableWiFi: Button
    private lateinit var btnClose: ImageButton

    interface InternetConnectionListener {
        fun onInternetConnectionFragmentClosed()
    }

    private var listener: InternetConnectionListener? = null

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
        listener?.onInternetConnectionFragmentClosed()
        requireActivity().supportFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

    fun setInternetConnectionListener(listener: InternetConnectionListener) {
        this.listener = listener
    }
}