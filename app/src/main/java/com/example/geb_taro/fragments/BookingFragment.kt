package com.example.geb_taro.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.geb_taro.databinding.FragmentBookingBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private var selectedDate = ""
    private var selectedTime = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding.btnDate.setOnClickListener {
            val now = Calendar.getInstance()

            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val date = Calendar.getInstance().apply {
                        set(year, month, day)
                    }

                    selectedDate = SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale("id", "ID")
                    ).format(date.time)

                    binding.btnDate.text = selectedDate
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnTime.setOnClickListener {
            val now = Calendar.getInstance()

            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    selectedTime = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                    )

                    binding.btnTime.text = selectedTime
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.btnConfirmBooking.setOnClickListener {
            if (selectedDate.isBlank() || selectedTime.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Pilih tanggal dan waktu terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val console =
                    binding.spinnerConsole.selectedItem.toString()

                val duration =
                    binding.spinnerDuration.selectedItem.toString()

                Toast.makeText(
                    requireContext(),
                    "Konsol: $console\n" +
                            "Tanggal: $selectedDate\n" +
                            "Jam: $selectedTime\n" +
                            "Durasi: $duration\n" +
                            "Belum tersimpan ke database.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
