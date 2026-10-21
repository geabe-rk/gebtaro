package com.example.geb_taro.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.geb_taro.DeveloperActivity
import com.example.geb_taro.LoginActivity
import com.example.geb_taro.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding.btnDeveloper.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    DeveloperActivity::class.java
                )
            )
        }

        binding.btnProfileLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage(
                    "Apakah kamu yakin ingin keluar dari IZIReps?"
                )
                .setNegativeButton("Batal", null)
                .setPositiveButton("Ya") { _, _ ->

                    requireContext()
                        .getSharedPreferences(
                            "user_pref",
                            Context.MODE_PRIVATE
                        )
                        .edit()
                        .putBoolean("isLogin", false)
                        .apply()

                    val intent = Intent(
                        requireContext(),
                        LoginActivity::class.java
                    ).apply {
                        flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }

                    startActivity(intent)
                    requireActivity().finish()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
