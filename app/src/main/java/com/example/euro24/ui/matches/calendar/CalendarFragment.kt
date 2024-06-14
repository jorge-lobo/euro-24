package com.example.euro24.ui.matches.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import com.example.euro24.R
import com.example.euro24.databinding.FragmentCalendarBinding
import com.example.euro24.ui.common.BaseFragment
import com.example.euro24.ui.matches.calendar.selectedDay.SelectedDayFragment
import java.util.Calendar

class CalendarFragment : BaseFragment() {

    private lateinit var binding: FragmentCalendarBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendar()
    }

    private fun setupCalendar() {
        setupMonthCalendar(
            Calendar.JUNE,
            binding.calendarGridJune,
            listOf(14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 29, 30)
        )
        setupMonthCalendar(Calendar.JULY, binding.calendarGridJuly, listOf(1, 2, 5, 6, 9, 10, 14))
    }

    private fun setupMonthCalendar(month: Int, gridView: GridView, events: List<Int>) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2024)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val firstDayOfJune = calendar.get(Calendar.DAY_OF_WEEK)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val days = mutableListOf<Int?>()

        // Add empty days before first day of the month
        for (i in 0 until firstDayOfJune - Calendar.SUNDAY) {
            days.add(null)
        }

        for (i in 1..daysInMonth) {
            days.add(i)
        }

        val adapter = CalendarAdapter(requireContext(), days, events)
        gridView.adapter = adapter

        gridView.post {
            adjustGridViewHeight(gridView)
        }

        gridView.setOnItemClickListener { _, view, position, _ ->
            val day = days[position]

            if (day != null && view.isEnabled) {
                openSelectedDayFragment(day, month)
            }
        }
    }

    private fun adjustGridViewHeight(gridView: GridView) {
        val listAdapter = gridView.adapter ?: return

        var totalHeight = 0
        var rowHeight: Int
        val items = listAdapter.count
        val numColumns = gridView.numColumns

        for (i in 0 until items) {
            val listItem = listAdapter.getView(i, null, gridView)
            listItem.measure(
                View.MeasureSpec.makeMeasureSpec(gridView.width, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.UNSPECIFIED
            )
            if (i % numColumns == 0) {
                rowHeight = listItem.measuredHeight
                totalHeight += rowHeight
            }
        }

        val params = gridView.layoutParams
        params.height = totalHeight + gridView.verticalSpacing * ((items / numColumns) - 1)
        gridView.layoutParams = params
        gridView.requestLayout()
    }

    private fun openSelectedDayFragment(day: Int, month: Int) {
        val fragment = SelectedDayFragment.newInstance(day, month)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}