package com.biz.evaluation3groceriesapp.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import com.biz.evaluation3groceriesapp.R

class LoginOptionsActivity : AppCompatActivity() {
    lateinit var emailButton: CardView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginoptions)

        initVar()

        onClick()

        onBackPress()

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