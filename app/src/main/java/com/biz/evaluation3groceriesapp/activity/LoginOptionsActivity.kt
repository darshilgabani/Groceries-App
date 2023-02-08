package com.biz.evaluation3groceriesapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.biz.evaluation3groceriesapp.R

class LoginOptionsActivity : AppCompatActivity() {
    lateinit var emailButton: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginoptions)

        initVar()

        onClick()
    }

    private fun onClick() {
        emailButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun initVar() {
        emailButton = findViewById(R.id.emailButton)
    }
}