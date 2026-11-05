package com.example.geb_taro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.geb_taro.BookingReminderManager
import com.example.geb_taro.R
import com.example.geb_taro.databinding.FragmentHistoryBinding
import com.google.android.material.button.MaterialButton

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(
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

        renderBookings()
    }

    override fun onResume() {

        super.onResume()

        if (_binding != null) {
            renderBookings()
        }
    }

    private fun renderBookings() {

        val context = context ?: return

        val bookings =
            BookingReminderManager.getBookings(context)

        // Tampilkan empty state jika belum ada booking.
        binding.cardHistoryEmpty.visibility =
            if (bookings.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }

        // Bersihkan daftar sebelumnya.
        binding.bookingsContainer.removeAllViews()

        bookings.forEach { booking ->

            // Kartu booking.
            val card = LinearLayout(context).apply {

                orientation = LinearLayout.VERTICAL

                setPadding(
                    dp(16),
                    dp(16),
                    dp(16),
                    dp(16)
                )

                setBackgroundColor(
                    android.graphics.Color.rgb(
                        16,
                        25,
                        35
                    )
                )

                layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = dp(12)
                    }
            }

            // Nama konsol.
            val title = TextView(context).apply {

                text = booking.console

                textSize = 18f

                setTextColor(
                    android.graphics.Color.WHITE
                )

                setTypeface(
                    typeface,
                    android.graphics.Typeface.BOLD
                )
            }

            // Informasi booking.
            val detail = TextView(context).apply {

                text =
                    "${BookingReminderManager.formatDateTime(booking.dateTimeMillis)}\n" +
                            "Durasi: ${booking.duration}\n" +
                            "Status: ${booking.status}\n" +
                            "Pengingat: 30 menit sebelum jadwal"

                textSize = 14f

                setTextColor(
                    android.graphics.Color.LTGRAY
                )

                setPadding(
                    0,
                    dp(8),
                    0,
                    dp(12)
                )
            }

            // Tombol membatalkan booking.
            val cancelButton =
                MaterialButton(context).apply {

                    text = "Batalkan Booking"

                    setTextColor(
                        android.graphics.Color.rgb(
                            5,
                            10,
                            15
                        )
                    )

                    setBackgroundColor(
                        android.graphics.Color.rgb(
                            255,
                            105,
                            105
                        )
                    )

                    setOnClickListener {

                        androidx.appcompat.app.AlertDialog
                            .Builder(requireContext())
                            .setTitle("Batalkan booking?")
                            .setMessage(
                                "Booking ini akan dihapus dan pengingatnya dibatalkan."
                            )
                            .setNegativeButton(
                                "Tidak",
                                null
                            )
                            .setPositiveButton(
                                "Ya, batalkan"
                            ) { _, _ ->

                                BookingReminderManager.cancelBooking(
                                    requireContext(),
                                    booking.id
                                )

                                Toast.makeText(
                                    requireContext(),
                                    "Booking dibatalkan",
                                    Toast.LENGTH_SHORT
                                ).show()

                                renderBookings()
                            }
                            .show()
                    }
                }

            // Masukkan komponen ke kartu.
            card.addView(title)

            card.addView(detail)

            card.addView(cancelButton)

            // Masukkan kartu ke daftar booking.
            binding.bookingsContainer.addView(card)
        }
    }

    private fun dp(value: Int): Int {

        return (
                value * resources.displayMetrics.density
                ).toInt()
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}