package com.example.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.expensetracker.R
import com.example.expensetracker.activities.auth.LoginActivity
import java.util.Locale


class StartUpActivity : AppCompatActivity() {

    var button_get_started: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)
        window.statusBarColor = ContextCompat.getColor(this, R.color.button_color)
        button_get_started = findViewById(R.id.button_get_started)
        button_get_started?.setOnClickListener{
            startActivity(
                Intent(
                    this@StartUpActivity,
                    LoginActivity::class.java
                )
            )
        }

    }

}