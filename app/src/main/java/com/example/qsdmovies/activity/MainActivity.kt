package com.example.qsdmovies.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.qsdmovies.R
import com.example.qsdmovies.fragment.FavoritesFragment
import com.example.qsdmovies.fragment.HomeFragment
import com.example.qsdmovies.fragment.SearchFragment
import com.example.qsdmovies.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var auth: FirebaseAuth

    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val favoritesFragment = FavoritesFragment()
    private val profileFragment = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        auth = FirebaseAuth.getInstance()

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.search -> replaceFragment(searchFragment)
                R.id.favorites -> replaceFragment(favoritesFragment)
                R.id.profile -> replaceFragment(profileFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.commit()
        }
    }
}