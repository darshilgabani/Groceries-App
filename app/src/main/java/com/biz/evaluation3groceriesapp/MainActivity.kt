package com.biz.evaluation3groceriesapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("GroceriesApp", MODE_PRIVATE)

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
//        navController = navHostFragment.navController
//
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        setupWithNavController(bottomNavigationView, navController)


        val navController = this.findNavController(R.id.fragmentContainerView)
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when(destination.id){
                R.id.productDetailsFragment2 -> navView.visibility = View.GONE
                R.id.categoriesProductFragment -> navView.visibility = View.GONE
                R.id.orderAcceptFragment -> navView.visibility = View.GONE
                R.id.orderListFragment -> navView.visibility = View.GONE
                R.id.orderDetailsFragment -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }

        }

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.shopFragment -> {
                    navController.navigate(R.id.shopFragment)
                    true
                }
                R.id.exploreFragment -> {
                    navController.navigate(R.id.exploreFragment)
                    true
                }
                R.id.cartFragment -> {
                    navController.navigate(R.id.cartFragment)
                    true
                }
                R.id.favouriteFragment -> {
                    navController.navigate(R.id.favouriteFragment)
                    true
                }
                R.id.accountFragment -> {
                    navController.navigate(R.id.accountFragment)
                    true
                }
                else -> false
            }
        }


    }


}