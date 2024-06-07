package com.example.euro24.ui.matches.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.euro24.R
import com.example.euro24.databinding.FragmentCalendarBinding
import com.example.euro24.ui.common.BaseFragment
import java.util.Calendar

class CalendarFragment : BaseFragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val mCalendarViewModel by lazy { ViewModelProvider(this)[CalendarViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_calendar,
            container,
            false
        )
        return binding.root
    }

}