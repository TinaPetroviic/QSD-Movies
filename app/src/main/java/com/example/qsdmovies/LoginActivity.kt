package com.example.qsdmovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var register : TextView
    private lateinit var forgotPassword : TextView
    private lateinit var emailHere : EditText
    private lateinit var passwordHere : EditText
    private lateinit var loginButton : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d("LoginActivity", "onCreate")

        register = findViewById(R.id.register)
        forgotPassword = findViewById(R.id.forgotPassword)
        emailHere = findViewById(R.id.emailHere)
        passwordHere = findViewById(R.id.passwordHere)
        loginButton = findViewById(R.id.loginButton)

        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            login()
        }
        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        forgotPassword.setOnClickListener {
            val intent = Intent(this,ForgotActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        val email = emailHere.text.toString()
        val password = passwordHere.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
        }
    }
}

