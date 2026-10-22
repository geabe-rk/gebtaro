package com.example.geb_taro

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtNama: EditText
    private lateinit var edtEmail: EditText
    private lateinit var tvTanggalLahir: TextView
    private lateinit var rgJenisKelamin: RadioGroup
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText

    private var tanggalLahir = ""

    private val preferences by lazy {
        getSharedPreferences("user_pref", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi komponen
        edtNama = findViewById(R.id.edtNama)
        edtEmail = findViewById(R.id.edtEmail)
        tvTanggalLahir = findViewById(R.id.tvTanggalLahir)
        rgJenisKelamin = findViewById(R.id.rgJenisKelamin)
        edtUsername = findViewById(R.id.edtUsernameRegister)
        edtPassword = findViewById(R.id.edtPasswordRegister)
        edtConfirmPassword =
            findViewById(R.id.edtConfirmPassword)

        val btnPilihTanggal =
            findViewById<Button>(R.id.btnPilihTanggal)

        val btnSelanjutnya =
            findViewById<Button>(R.id.btnSelanjutnya)

        val tvKembaliLogin =
            findViewById<TextView>(R.id.tvKembaliLogin)

        // Isi ulang data jika sebelumnya sudah pernah disimpan
        isiFormDariSharedPreferences()

        // Pilih tanggal lahir
        btnPilihTanggal.setOnClickListener {
            tampilkanDatePicker()
        }

        tvTanggalLahir.setOnClickListener {
            tampilkanDatePicker()
        }

        // Kembali ke halaman Login
        tvKembaliLogin.setOnClickListener {
            finish()
        }

        // Simpan data dan lanjut ke halaman Verifikasi
        btnSelanjutnya.setOnClickListener {
            simpanRegistrasi()
        }
    }

    private fun tampilkanDatePicker() {
        val kalender = Calendar.getInstance()

        val dialog = DatePickerDialog(
            this,
            { _, tahun, bulan, hari ->
                tanggalLahir = String.format(
                    "%04d-%02d-%02d",
                    tahun,
                    bulan + 1,
                    hari
                )

                tvTanggalLahir.text = tanggalLahir
            },
            kalender.get(Calendar.YEAR),
            kalender.get(Calendar.MONTH),
            kalender.get(Calendar.DAY_OF_MONTH)
        )

        // Tanggal lahir tidak boleh berada di masa depan
        dialog.datePicker.maxDate = System.currentTimeMillis()
        dialog.show()
    }

    private fun simpanRegistrasi() {
        val nama = edtNama.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString()
        val confirmPassword =
            edtConfirmPassword.text.toString()

        val jenisKelamin = when (
            rgJenisKelamin.checkedRadioButtonId
        ) {
            R.id.rbLakiLaki -> "Laki-laki"
            R.id.rbPerempuan -> "Perempuan"
            else -> ""
        }

        // Validasi semua input wajib diisi
        if (
            nama.isBlank() ||
            email.isBlank() ||
            tanggalLahir.isBlank() ||
            jenisKelamin.isBlank() ||
            username.isBlank() ||
            password.isBlank() ||
            confirmPassword.isBlank()
        ) {
            Toast.makeText(
                this,
                "Semua input wajib diisi",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Validasi format email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.error = "Format email tidak valid"
            edtEmail.requestFocus()
            return
        }

        // Validasi kecocokan password
        if (password != confirmPassword) {
            edtConfirmPassword.error =
                "Password dan Confirm Password harus sama"
            edtConfirmPassword.requestFocus()
            return
        }

        val berhasil = preferences.edit()
            .putString("nama", nama)
            .putString("email", email)
            .putString("tanggal_lahir", tanggalLahir)
            .putString("jenis_kelamin", jenisKelamin)
            .putString("username", username)
            .putString("password", password)
            .putString("confirm_password", confirmPassword)
            // Data baru harus diverifikasi ulang
            .putBoolean("registrationVerified", false)
            .commit()

        if (!berhasil) {
            Toast.makeText(
                this,
                "Data registrasi gagal disimpan",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Toast.makeText(
            this,
            "Data registrasi berhasil disimpan",
            Toast.LENGTH_SHORT
        ).show()

        // Buka halaman verifikasi registrasi
        startActivity(
            Intent(this, VerificationActivity::class.java)
        )
    }

    private fun isiFormDariSharedPreferences() {
        edtNama.setText(preferences.getString("nama", ""))
        edtEmail.setText(preferences.getString("email", ""))

        val tanggal =
            preferences.getString("tanggal_lahir", "").orEmpty()

        if (tanggal.isNotBlank()) {
            tanggalLahir = tanggal
            tvTanggalLahir.text = tanggal
        }

        when (preferences.getString("jenis_kelamin", "")) {
            "Laki-laki" -> {
                rgJenisKelamin.check(R.id.rbLakiLaki)
            }

            "Perempuan" -> {
                rgJenisKelamin.check(R.id.rbPerempuan)
            }
        }

        edtUsername.setText(preferences.getString("username", ""))
        edtPassword.setText(preferences.getString("password", ""))

        edtConfirmPassword.setText(
            preferences.getString("confirm_password", "")
        )
    }
}
