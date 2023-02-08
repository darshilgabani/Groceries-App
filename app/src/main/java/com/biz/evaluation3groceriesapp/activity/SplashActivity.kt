package com.biz.evaluation3groceriesapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.biz.evaluation3groceriesapp.MainActivity
import com.biz.evaluation3groceriesapp.R

class SplashActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editSharedPreferences: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initVar()

        splashScreenFun()

    }

    private fun splashScreenFun() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            checkLogin()
            finish()
        },1000)
    }

    private fun initVar() {
        sharedPreferences = getSharedPreferences("Login Data", MODE_PRIVATE)
        editSharedPreferences = sharedPreferences.edit()
    }

    private fun checkLogin(){
        val data = sharedPreferences.getInt("LoggedIn",10)
        if (data == 1){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            startActivity(Intent(this, WelcomeActivity::class.java))
        }
    }
}