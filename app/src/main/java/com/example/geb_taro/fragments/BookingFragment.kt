package com.example.geb_taro.fragments

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.geb_taro.BookingNotificationHelper
import com.example.geb_taro.BookingReminderItem
import com.example.geb_taro.BookingReminderManager
import com.example.geb_taro.databinding.FragmentBookingBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null

    private val binding get() = _binding!!

    private var selectedDate = ""
    private var selectedTime = ""

    private var selectedDateCalendar: Calendar? = null

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (!granted && isAdded) {
                Toast.makeText(
                    requireContext(),
                    "Izin notifikasi ditolak. Booking tetap tersimpan, tetapi pengingat tidak dapat ditampilkan.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBookingBinding.inflate(
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

        // Buat kanal notifikasi.
        BookingNotificationHelper.createChannel(
            requireContext()
        )

        // Tombol pilih tanggal.
        binding.btnDate.setOnClickListener {

            val now = Calendar.getInstance()

            val initial =
                selectedDateCalendar ?: now

            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->

                    selectedDateCalendar =
                        Calendar.getInstance().apply {

                            set(Calendar.YEAR, year)

                            set(Calendar.MONTH, month)

                            set(Calendar.DAY_OF_MONTH, day)

                            set(Calendar.HOUR_OF_DAY, 0)

                            set(Calendar.MINUTE, 0)

                            set(Calendar.SECOND, 0)

                            set(Calendar.MILLISECOND, 0)
                        }

                    selectedDate =
                        SimpleDateFormat(
                            "dd MMM yyyy",
                            Locale("id", "ID")
                        ).format(
                            selectedDateCalendar!!.time
                        )

                    binding.btnDate.text = selectedDate
                },
                initial.get(Calendar.YEAR),
                initial.get(Calendar.MONTH),
                initial.get(Calendar.DAY_OF_MONTH)
            ).apply {

                // Tidak mengizinkan pemilihan tanggal lampau.
                datePicker.minDate = now.timeInMillis

            }.show()
        }

        // Tombol pilih waktu.
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

        // Tombol konfirmasi booking.
        binding.btnConfirmBooking.setOnClickListener {
            saveBookingAndScheduleReminder()
        }
    }

    private fun saveBookingAndScheduleReminder() {

        val dateCalendar = selectedDateCalendar

        // Pastikan tanggal dan waktu sudah dipilih.
        if (
            selectedDate.isBlank() ||
            selectedTime.isBlank() ||
            dateCalendar == null
        ) {

            Toast.makeText(
                requireContext(),
                "Pilih tanggal dan waktu terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val timeParts = selectedTime.split(":")

        if (timeParts.size != 2) {
            return
        }

        // Gabungkan tanggal dan waktu menjadi satu waktu booking.
        val bookingCalendar =
            (dateCalendar.clone() as Calendar).apply {

                set(
                    Calendar.HOUR_OF_DAY,
                    timeParts[0].toInt()
                )

                set(
                    Calendar.MINUTE,
                    timeParts[1].toInt()
                )

                set(Calendar.SECOND, 0)

                set(Calendar.MILLISECOND, 0)
            }

        val bookingTimeMillis =
            bookingCalendar.timeInMillis

        // Booking harus berada di masa mendatang.
        if (
            bookingTimeMillis <=
            System.currentTimeMillis()
        ) {

            Toast.makeText(
                requireContext(),
                "Jadwal booking harus berada di waktu mendatang",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        // Bentuk objek booking.
        val booking = BookingReminderItem(
            id = System.currentTimeMillis(),
            console = binding.spinnerConsole
                .selectedItem.toString(),
            dateTimeMillis = bookingTimeMillis,
            duration = binding.spinnerDuration
                .selectedItem.toString()
        )

        // Simpan booking secara lokal.
        BookingReminderManager.saveBooking(
            requireContext(),
            booking
        )

        // Jadwalkan pengingat 30 menit sebelumnya.
        val reminderScheduled =
            BookingReminderManager.scheduleReminder(
                requireContext(),
                booking
            )

        // Minta izin notifikasi jika diperlukan.
        requestNotificationPermissionIfNeeded()

        val message = if (reminderScheduled) {

            "Booking berhasil disimpan! Pengingat dijadwalkan 30 menit sebelum bermain."

        } else {

            "Booking berhasil disimpan, tetapi waktunya kurang dari 30 menit lagi sehingga pengingat tidak dijadwalkan."
        }

        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun requestNotificationPermissionIfNeeded() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU &&

            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}