package com.example.geb_taro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.geb_taro.databinding.ActivityBaseBinding
import com.example.geb_taro.fragments.BookingFragment
import com.example.geb_taro.fragments.FoodFragment
import com.example.geb_taro.fragments.HistoryFragment
import com.example.geb_taro.fragments.HomeFragment
import com.example.geb_taro.fragments.ProfileFragment

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_booking -> BookingFragment()
                R.id.nav_history -> HistoryFragment()
                R.id.nav_food -> FoodFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

            true
        }

        if (savedInstanceState == null) {
            binding.bottomNavView.selectedItemId = R.id.nav_home
        }
    }
}
