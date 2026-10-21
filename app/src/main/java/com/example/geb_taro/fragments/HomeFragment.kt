package com.example.geb_taro.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.geb_taro.BaseActivity
import com.example.geb_taro.R
import com.example.geb_taro.WebActivity
import com.example.geb_taro.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHomeBooking.setOnClickListener {
            selectTab(R.id.nav_booking)
        }

        binding.btnHomeFood.setOnClickListener {
            selectTab(R.id.nav_food)
        }

        binding.btnHomeWeb.setOnClickListener {
            startActivity(
                Intent(requireContext(), WebActivity::class.java)
            )
        }
    }

    private fun selectTab(itemId: Int) {
        val bottomNav = requireActivity()
            .findViewById<BottomNavigationView>(
                R.id.bottom_nav_view
            )

        bottomNav.selectedItemId = itemId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
