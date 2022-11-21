package com.example.qsdmovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var register : TextView
    private lateinit var forgotPassword : TextView
    private lateinit var emailHere : EditText
    private lateinit var passwordHere : EditText
    private lateinit var loginButton : Button

    private lateinit var auth: FirebaseAuth

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register = findViewById(R.id.register)
        forgotPassword = findViewById(R.id.forgotPassword)
        emailHere = findViewById(R.id.emailHere)
        passwordHere = findViewById(R.id.passwordHere)
        loginButton = findViewById(R.id.loginButton)

        auth = Firebase.auth

        loginButton.setOnClickListener {
            login()
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        if(emailHere.text.isEmpty() || passwordHere.text.isEmpty()){
            Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT)
                .show()
            return
        }

        val email = emailHere.text.toString()
        val pass = passwordHere.text.toString()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                val intent = Intent (this,HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}