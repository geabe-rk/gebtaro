package com.example.geb_taro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtUsername =
            findViewById<EditText>(R.id.edtUsername)

        val edtPassword =
            findViewById<EditText>(R.id.edtPassword)

        val btnLogin =
            findViewById<Button>(R.id.btnLogin)

        val tvDeveloper =
            findViewById<TextView>(R.id.tvDeveloper)

        val tvRegister =
            findViewById<TextView>(R.id.tvRegister)

        val preferences = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        )

        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString()

            // 1. Periksa input kosong
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(
                    this,
                    "Username dan password wajib diisi",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // 2. Ambil akun yang tersimpan saat registrasi
            val usernameTersimpan =
                preferences.getString("username", "").orEmpty()

            val passwordTersimpan =
                preferences.getString("password", "").orEmpty()

            val registrasiVerified =
                preferences.getBoolean(
                    "registrationVerified",
                    false
                )

            // 3. Aturan login praktikum sebelumnya
            val loginPraktikum =
                username == password

            // 4. Aturan login menggunakan akun terdaftar
            val loginRegistrasi =
                usernameTersimpan.isNotBlank() &&
                        passwordTersimpan.isNotBlank() &&
                        username == usernameTersimpan &&
                        password == passwordTersimpan &&
                        registrasiVerified

            // 5. Login berhasil jika salah satu aturan terpenuhi
            if (loginPraktikum || loginRegistrasi) {

                preferences.edit()
                    .putBoolean("isLogin", true)
                    .apply()

                Toast.makeText(
                    this,
                    "Login berhasil",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(
                    this,
                    BaseActivity::class.java
                )

                startActivity(intent)
                finish()

            } else {
                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage(
                        "Username atau password tidak sesuai, " +
                                "atau akun belum melewati verifikasi."
                    )
                    .setPositiveButton("OK", null)
                    .show()
            }
        }

        // Buka halaman registrasi
        tvRegister.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }

        // Buka halaman developer
        tvDeveloper.setOnClickListener {
            startActivity(
                Intent(this, DeveloperActivity::class.java)
            )
        }
    }
}
