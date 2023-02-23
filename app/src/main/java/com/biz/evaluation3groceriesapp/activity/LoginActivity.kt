package com.biz.evaluation3groceriesapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.biz.evaluation3groceriesapp.MainActivity
import com.biz.evaluation3groceriesapp.R
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private lateinit var passwordEdit: String
    private lateinit var emailEdit: String
    private lateinit var email: String
    private lateinit var password: String

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: CardView
    lateinit var loginProgressBar: ProgressBar

    lateinit var databaseReference: DatabaseReference

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editSharedPreferences: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initVar()

        onClick()

    }

    private fun onBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    private fun onClick() {

        loginButton.setOnClickListener {

            emailEdit = emailEditText.text.toString()
            passwordEdit = passwordEditText.text.toString()
            
            if (emailEdit =="" && passwordEdit ==""){
                Toast.makeText(this, "Please Enter Email and Password!", Toast.LENGTH_SHORT).show()
            }else{
                loginProgressBar.visibility = View.VISIBLE
                databaseReference.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        email = snapshot.child("email").value.toString()
                        password = snapshot.child("password").value.toString()
                        loginValidation()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }

    }

    private fun loginValidation() {
        if (emailEdit == email && passwordEdit == password) {
            editSharedPreferences.putInt("loginPref", 1).apply()
            loginProgressBar.visibility = View.INVISIBLE
            startActivity(Intent(this, MainActivity::class.java))
            emailEditText.setText("")
            passwordEditText.setText("")
        } else {
            loginProgressBar.visibility = View.INVISIBLE
            Toast.makeText(this@LoginActivity, "Please Enter Valid User Credentials ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initVar() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        loginProgressBar = findViewById(R.id.loginProgressBar)

        sharedPreferences = getSharedPreferences("GroceriesApp", MODE_PRIVATE)
        editSharedPreferences = sharedPreferences.edit()

        databaseReference = FirebaseDatabase.getInstance().getReference("LoginCredentials")

        if (sharedPreferences.getInt("loginPref", 0) == 2) {
            onBackPress()
        }
    }
}