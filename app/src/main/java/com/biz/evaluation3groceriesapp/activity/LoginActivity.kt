package com.biz.evaluation3groceriesapp.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.biz.evaluation3groceriesapp.MainActivity
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.modelclass.LoginCredentialsData
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    lateinit var emailEditText : EditText
    lateinit var passwordEditText : EditText
    lateinit var loginButton : CardView
    lateinit var loginProgressBar : ProgressBar

    lateinit var databaseReference: DatabaseReference

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editSharedPreferences: SharedPreferences.Editor

    var loginData : LoginCredentialsData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initVar()

        onClick()

    }

    private fun onClick() {
        loginButton.setOnClickListener {
            loginProgressBar.visibility = View.VISIBLE
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                loginData = snapshot.getValue(LoginCredentialsData::class.java)
                loginValidation()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        }

    }

    private fun loginValidation(){
        val emailEdit = emailEditText.text.toString()
        val passwordEdit = passwordEditText.text.toString()
        val emailDb = loginData?.email
        val passwordDb = loginData?.password

        if (emailEdit == emailDb && passwordEdit == passwordDb ){
            emailEditText.setText("")
            passwordEditText.setText("")

            editSharedPreferences.putInt("LoggedIn",1).apply()
            loginProgressBar.visibility = View.INVISIBLE
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            Toast.makeText(this@LoginActivity, "Please Enter Valid User Credentials ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initVar() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        loginProgressBar = findViewById(R.id.loginProgressBar)

        sharedPreferences = getSharedPreferences("Login Data", MODE_PRIVATE)
        editSharedPreferences = sharedPreferences.edit()

        databaseReference = FirebaseDatabase.getInstance().getReference("LoginCredentials")
    }
}