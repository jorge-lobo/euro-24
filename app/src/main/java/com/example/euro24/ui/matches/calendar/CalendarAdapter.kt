package com.example.euro24.ui.matches.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.euro24.R

class CalendarAdapter(
    context: Context,
    days: List<Int?>,
    private val events: List<Int>
) :
    ArrayAdapter<Int?>(context, 0, days) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val day = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.grid_item_calendar_day, parent, false)
        val textView = view.findViewById<TextView>(R.id.text_item_day)

        if (day != null) {
            textView.text = day.toString()
            if (events.contains(day)) {
                textView.setTextColor(ContextCompat.getColor(context, R.color.calendar_day_active))
                view.isEnabled = true
            } else {
                textView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.calendar_day_inactive
                    )
                )
                view.isEnabled = false
            }
        } else {
            textView.text = ""
            view.isEnabled = false
        }

        return view
    }
}