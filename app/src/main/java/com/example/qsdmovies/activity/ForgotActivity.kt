package com.example.qsdmovies.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityForgotBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot.*


class ForgotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotBinding

    private lateinit var auth: FirebaseAuth
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "BACK"


        auth = FirebaseAuth.getInstance()

        binding.sendEmailButton.setOnClickListener {
            val email = emailForgot.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "email field can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (email.matches(emailPattern.toRegex())) {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Please Check your Email", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ForgotActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(
                    applicationContext, "invalid email address",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            super.onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}


