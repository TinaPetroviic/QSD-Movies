package com.example.qsdmovies.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.qsdmovies.R
import com.example.qsdmovies.databinding.ActivityBottombarBinding
import com.example.qsdmovies.fragment.FavoritesFragment
import com.example.qsdmovies.fragment.HomeFragment
import com.example.qsdmovies.fragment.ProfileFragment
import com.example.qsdmovies.fragment.SearchFragment
import com.google.firebase.auth.FirebaseAuth


class BottomBarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottombarBinding

    private var backPressedTime = 0L

    private lateinit var auth: FirebaseAuth

    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val favoritesFragment = FavoritesFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottombarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        loadFragment(HomeFragment())

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
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
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(applicationContext, getString(R.string.press_back_again_to_exit_app) , Toast.LENGTH_SHORT)
                .show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}