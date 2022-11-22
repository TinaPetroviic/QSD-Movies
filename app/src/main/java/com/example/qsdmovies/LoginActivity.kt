package com.example.qsdmovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailHere: EditText
    private lateinit var passwordHere: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPassword: TextView
    private lateinit var register: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailHere = findViewById(R.id.emailHere)
        passwordHere = findViewById(R.id.passwordHere)
        loginButton = findViewById(R.id.loginButton)
        forgotPassword = findViewById(R.id.forgotPassword)
        register = findViewById(R.id.register)

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
            val int = Intent(this, ForgotActivity::class.java)
            startActivity(int)
            finish()
        }
    }

    private fun login() {

        val email = emailHere.text.toString()
        val pass = passwordHere.text.toString()

        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
        }
    }
}