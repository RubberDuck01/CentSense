package com.example.expensetracker.activities.about

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.expensetracker.R

class AboutActivity : AppCompatActivity() {

    var image_back: ImageView? = null
    var text_app_version: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        window.statusBarColor = ContextCompat.getColor(this, R.color.button_color)

        image_back = findViewById(R.id.image_back)
        text_app_version = findViewById(R.id.text_app_version)

        image_back?.setOnClickListener{
            onBackPressed()
            finish()
        }

        text_app_version?.text = "v${getAppVersion()} // Build: 210823"

    }

    private fun getAppVersion(): String {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Unknown"
    }

}