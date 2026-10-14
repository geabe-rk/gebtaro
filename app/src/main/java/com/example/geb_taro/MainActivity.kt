
package com.example.geb_taro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.cardview.widget.CardView
import androidx.appcompat.app.AlertDialog

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = false

        setContentView(R.layout.activity_main)

        // Book Console
        findViewById<CardView>(R.id.btnBookConsole)
            .setOnClickListener {
                startActivity(
                    Intent(this, activity_penjelasan::class.java)
                )
            }

        // Food
        findViewById<CardView>(R.id.btnFood)
            .setOnClickListener {
                startActivity(
                    Intent(this, FoodActivity::class.java)
                )
            }

        // Profile & History
        findViewById<CardView>(R.id.btnProfile)
            .setOnClickListener {
                startActivity(
                    Intent(this, ProfileActivity::class.java)
                )
            }

        // WebView
        findViewById<Button>(R.id.btnWeb)
            .setOnClickListener {
                startActivity(
                    Intent(this, WebActivity::class.java)
                )
            }

        // Logout
        findViewById<Button>(R.id.btnLogout)
            .setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Logout")
                    .setMessage("Apakah kamu yakin ingin keluar?")
                    .setNegativeButton("Batal", null)
                    .setPositiveButton("Ya") { _, _ ->

                        getSharedPreferences(
                            "user_pref",
                            MODE_PRIVATE
                        ).edit()
                            .putBoolean("isLogin", false)
                            .apply()

                        val intent = Intent(
                            this,
                            LoginActivity::class.java
                        ).apply {
                            flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }

                        startActivity(intent)
                    }
                    .show()
            }
    }
}
