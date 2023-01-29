package com.example.qsdmovies.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.R
import com.example.qsdmovies.databinding.ActivityForgotBinding
import com.example.qsdmovies.util.Constants
import com.google.firebase.auth.FirebaseAuth


class ForgotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.clBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSendEmail.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.email_field_cant_be_empty),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (email.matches(Constants.EMAIL_PATTERN.toRegex())) {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            getString(R.string.please_check_your_email),
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@ForgotActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(
                    applicationContext, getString(R.string.invalid_email_address),
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


