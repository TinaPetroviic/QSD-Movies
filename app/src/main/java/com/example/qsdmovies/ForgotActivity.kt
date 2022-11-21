package com.example.qsdmovies

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Suppress("UNUSED_EXPRESSION")
class ForgotActivity : AppCompatActivity() {

    private lateinit var emailForgot: EditText
    private lateinit var sendEmailButton: Button

    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        emailForgot = findViewById(R.id.emailForgot)
        sendEmailButton = findViewById(R.id.sendEmailButton)

        auth = Firebase.auth

        auth.sendPasswordResetEmail(emailForgot.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                "successful!" }
            else { "failed!"}
            }
        }
    }

