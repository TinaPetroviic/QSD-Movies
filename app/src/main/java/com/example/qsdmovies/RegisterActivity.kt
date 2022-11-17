package com.example.qsdmovies


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class RegisterActivity : AppCompatActivity() {


    private lateinit var firstNameHere : EditText
    private lateinit var lastNameHere : EditText
    private lateinit var emailHere : EditText
    private lateinit var registerPasswordHere : EditText
    private lateinit var confirmPasswordHere : EditText
    private lateinit var registerButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firstNameHere = findViewById(R.id.firstNameHere)
        lastNameHere = findViewById(R.id.lastNameHere)
        emailHere = findViewById(R.id.emailHere)
        registerPasswordHere = findViewById(R.id.registerPasswordHere)
        confirmPasswordHere = findViewById(R.id.confirmPasswordHere)
        registerButton = findViewById(R.id.registerButton)



    }
}