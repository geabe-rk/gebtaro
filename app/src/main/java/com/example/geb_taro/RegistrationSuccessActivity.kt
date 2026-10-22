package com.example.geb_taro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegistrationSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_success)

        val preferences = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        )

        val nama = preferences.getString("nama", "").orEmpty()

        findViewById<TextView>(R.id.tvSuccessMessage).text =
            "Selamat, $nama!\nRegistrasi Berhasil."

        findViewById<Button>(R.id.btnKembaliLogin)
            .setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NEW_TASK

                startActivity(intent)
                finish()
            }
    }
}
