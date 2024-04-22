package com.example.euro24.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {
    val currentDate: Date = Calendar.getInstance().time
    @SuppressLint("ConstantLocale")
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val datePreTournament: Date? = formatter.parse("14/01/2024")  // real date: 14/06/2024
    val datePostTournament: Date? = formatter.parse("14/03/2024")   // real date: 14/07/2024

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun startActivity(context: Context, activityClass: Class<*>) {
        context.startActivity(Intent(context, activityClass))
    }
}