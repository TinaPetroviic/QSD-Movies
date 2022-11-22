package com.example.qsdmovies

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ForgotActivity : AppCompatActivity() {

    private lateinit var emailForgot: EditText
    private lateinit var sendEmailButton: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        emailForgot = findViewById(R.id.emailForgot)
        sendEmailButton = findViewById(R.id.sendEmailButton)

        auth = Firebase.auth

        val actionbar = supportActionBar
        actionbar!!.title = "BACK"

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
