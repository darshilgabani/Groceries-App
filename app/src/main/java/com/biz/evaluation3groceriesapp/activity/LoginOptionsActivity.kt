package com.biz.evaluation3groceriesapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.biz.evaluation3groceriesapp.R

class LoginOptionsActivity : AppCompatActivity() {
    lateinit var emailButton: CardView
    lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginoptions)

        initVar()

        onClick()

        onBackPress()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            // Hide the navigation bar using the deprecated method
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }


    private fun onBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sharedPreferences.getInt("loginPref", 0) == 3) {
                    finishAffinity()
                }
            }
        })
    }

    private fun onClick() {
        emailButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun initVar() {
        emailButton = findViewById(R.id.emailButton)

        sharedPreferences = getSharedPreferences("GroceriesApp", MODE_PRIVATE)
    }
}