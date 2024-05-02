package com.example.euro24.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.euro24.R
import com.example.euro24.databinding.ActivityHistoryBinding
import com.example.euro24.ui.bottomNav.BottomNavFragment

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNav()

    }


    private fun setupBottomNav() {
        val bottomNavFragment = BottomNavFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.bottom_nav_fragment, bottomNavFragment)
            .commit()
    }
}