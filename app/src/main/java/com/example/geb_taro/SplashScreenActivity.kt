
package com.example.geb_taro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({

            val preferences =
                getSharedPreferences("user_pref", MODE_PRIVATE)

            val isLogin =
                preferences.getBoolean("isLogin", false)

            val destination = if (isLogin) {
                MainActivity::class.java
            } else {
                LoginActivity::class.java
            }

            startActivity(Intent(this, destination))
            finish()

        }, 2000)
    }
}
