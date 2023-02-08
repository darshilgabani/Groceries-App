package com.biz.evaluation3groceriesapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.biz.evaluation3groceriesapp.R

class WelcomeActivity : AppCompatActivity() {
    lateinit var getStartedButton : CardView

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
    }
}