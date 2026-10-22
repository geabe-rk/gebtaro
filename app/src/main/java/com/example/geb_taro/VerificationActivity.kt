package com.example.geb_taro

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VerificationActivity : AppCompatActivity() {

    private val preferences by lazy {
        getSharedPreferences("user_pref", MODE_PRIVATE)
    }

    private lateinit var tvDataRegistrasi: TextView
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        tvDataRegistrasi = findViewById(R.id.tvDataRegistrasi)
        edtUsername = findViewById(R.id.edtVerifikasiUsername)
        edtPassword = findViewById(R.id.edtVerifikasiPassword)
        edtConfirmPassword =
            findViewById(R.id.edtVerifikasiConfirmPassword)

        tampilkanDataRegistrasi()

        findViewById<Button>(R.id.btnSubmitVerifikasi)
            .setOnClickListener {
                validasiRegistrasi()
            }

        findViewById<Button>(R.id.btnKembaliRegistrasi)
            .setOnClickListener {
                startActivity(
                    Intent(this, RegisterActivity::class.java)
                )
                finish()
            }
    }

    private fun tampilkanDataRegistrasi() {
        val nama = preferences.getString("nama", "").orEmpty()
        val email = preferences.getString("email", "").orEmpty()
        val tanggalLahir =
            preferences.getString("tanggal_lahir", "").orEmpty()
        val jenisKelamin =
            preferences.getString("jenis_kelamin", "").orEmpty()
        val username =
            preferences.getString("username", "").orEmpty()

        tvDataRegistrasi.text = """
            DATA REGISTRASI TERSIMPAN

            Nama: $nama
            Email: $email
            Tanggal Lahir: $tanggalLahir
            Jenis Kelamin: $jenisKelamin
            Username: $username
        """.trimIndent()

        // Isi username secara otomatis agar pengguna mudah memeriksanya.
        edtUsername.setText(username)
    }

    private fun validasiRegistrasi() {
        val nama = preferences.getString("nama", "").orEmpty().trim()
        val email = preferences.getString("email", "").orEmpty().trim()
        val tanggalLahir =
            preferences.getString("tanggal_lahir", "").orEmpty().trim()
        val jenisKelamin =
            preferences.getString("jenis_kelamin", "").orEmpty().trim()
        val usernameTersimpan =
            preferences.getString("username", "").orEmpty().trim()
        val passwordTersimpan =
            preferences.getString("password", "").orEmpty()
        val confirmTersimpan =
            preferences.getString("confirm_password", "").orEmpty()

        val usernameInput = edtUsername.text.toString().trim()
        val passwordInput = edtPassword.text.toString()
        val confirmInput = edtConfirmPassword.text.toString()

        // Semua data utama di SharedPreferences wajib tersedia.
        if (
            nama.isBlank() ||
            email.isBlank() ||
            tanggalLahir.isBlank() ||
            jenisKelamin.isBlank() ||
            usernameTersimpan.isBlank() ||
            passwordTersimpan.isBlank() ||
            confirmTersimpan.isBlank()
        ) {
            Toast.makeText(
                this,
                "Data registrasi belum lengkap. Kembali dan perbaiki.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(
                this,
                "Email yang tersimpan tidak valid.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Semua input pada halaman verifikasi wajib diisi.
        if (
            usernameInput.isBlank() ||
            passwordInput.isBlank() ||
            confirmInput.isBlank()
        ) {
            Toast.makeText(
                this,
                "Semua input verifikasi wajib diisi.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (usernameInput != usernameTersimpan) {
            edtUsername.error = "Username tidak sesuai dengan data tersimpan"
            edtUsername.requestFocus()
            return
        }

        if (
            passwordTersimpan != confirmTersimpan ||
            passwordInput != confirmInput ||
            passwordInput != passwordTersimpan
        ) {
            edtConfirmPassword.error = "Password tidak cocok"
            edtConfirmPassword.requestFocus()
            return
        }

        // Tandai registrasi sudah lolos validasi.
        preferences.edit()
            .putBoolean("registrationVerified", true)
            .apply()

        startActivity(
            Intent(this, RegistrationSuccessActivity::class.java)
        )
        finish()
    }
}
