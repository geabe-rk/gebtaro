
package com.example.geb_taro

import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DeveloperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)

        val photo = findViewById<ImageView>(R.id.imgDeveloper)

        photo.clipToOutline = true
        photo.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(
                view: View,
                outline: android.graphics.Outline
            ) {
                outline.setOval(0, 0, view.width, view.height)
            }
        }

        photo.invalidateOutline()

        findViewById<Button>(R.id.btnBackLogin).setOnClickListener {
            finish()
        }
    }
}
