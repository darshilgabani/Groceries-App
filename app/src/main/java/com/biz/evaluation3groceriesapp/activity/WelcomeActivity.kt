package com.biz.evaluation3groceriesapp.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.biz.evaluation3groceriesapp.R

class WelcomeActivity : AppCompatActivity() {
    lateinit var getStartedButton : CardView

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editSharedPreferences: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initVar()

        onClick()

    }

    private fun onClick() {
        getStartedButton.setOnClickListener {
            startActivity(Intent(this,LoginOptionsActivity::class.java))
        }
    }

    private fun initVar() {
        getStartedButton = findViewById(R.id.getStartedButton)

        sharedPreferences = getSharedPreferences("GroceriesApp", MODE_PRIVATE)
        editSharedPreferences = sharedPreferences.edit()

        editSharedPreferences.putInt("loginPref", 3).apply()

    }
}