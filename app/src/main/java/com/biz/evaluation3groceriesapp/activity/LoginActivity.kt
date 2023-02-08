package com.biz.evaluation3groceriesapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.cardview.widget.CardView
import com.biz.evaluation3groceriesapp.MainActivity
import com.biz.evaluation3groceriesapp.R

class LoginActivity : AppCompatActivity() {
    lateinit var emailEditText : EditText
    lateinit var passwordEditText : EditText
    lateinit var loginButton : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initVar()

        onClick()

    }

    private fun onClick() {
        loginButton.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun initVar() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
    }
}