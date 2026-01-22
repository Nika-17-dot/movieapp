package com.example.movieapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
            androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
        )


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment


        val navController = navHostFragment.navController


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        bottomNav.setupWithNavController(navController)
    }
}