
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

        val edtUsername = findViewById<EditText>(R.id.edtUsername)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvDeveloper = findViewById<TextView>(R.id.tvDeveloper)

        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString()

            if (
                username.isNotEmpty() &&
                password.isNotEmpty() &&
                username == password
            ) {
                getSharedPreferences("user_pref", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isLogin", true)
                    .apply()

                Toast.makeText(
                    this,
                    "Login berhasil",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage(
                        "Username dan password harus terisi " +
                                "dan memiliki nilai yang sama. Silakan coba lagi."
                    )
                    .setPositiveButton("OK", null)
                    .show()
            }
        }

        tvDeveloper.setOnClickListener {
            startActivity(
                Intent(this, DeveloperActivity::class.java)
            )
        }
    }
}
