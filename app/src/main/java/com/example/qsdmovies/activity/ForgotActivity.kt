package com.example.qsdmovies.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.R
import com.google.firebase.auth.FirebaseAuth


class ForgotActivity : AppCompatActivity() {

    private lateinit var emailForgot: EditText
    private lateinit var sendEmailButton: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "BACK"

        emailForgot = findViewById(R.id.emailForgot)
        sendEmailButton = findViewById(R.id.sendEmailButton)

        auth = FirebaseAuth.getInstance()

        sendEmailButton.setOnClickListener {
            val email = emailForgot.text.toString()
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(this, "Please Check your Email", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
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


