package com.biz.evaluation3groceriesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                else -> navView.visibility = View.VISIBLE
            }

        }

    }
}