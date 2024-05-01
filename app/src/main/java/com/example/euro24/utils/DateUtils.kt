package com.example.euro24.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val currentDate: Date = Calendar.getInstance().time

    @SuppressLint("ConstantLocale")
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val datePreTournament: Date? = formatter.parse("14/06/2024")  // real date: 14/06/2024
    val datePostTournament: Date? = formatter.parse("14/07/2024")   // real date: 14/07/2024

    @JvmStatic
    fun formatDateShort(dateString: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, MMM dd", Locale.ENGLISH)
        val date = inputFormat.parse(dateString)
        return date?.let { outputFormat.format(it) } ?: ""
    }

    fun formatDateLong(date: Date): String {
        val outputFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH)
        return outputFormat.format(date)
    }

    @JvmStatic
    fun convertToTimeZone(time: String, format: String, timeZone: TimeZone): String {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("GMT+02:00")
        val outputFormat = SimpleDateFormat(format, Locale.getDefault())
        outputFormat.timeZone = timeZone
        val date = inputFormat.parse(time)
        return date?.let { outputFormat.format(it) } ?: ""
    }

}