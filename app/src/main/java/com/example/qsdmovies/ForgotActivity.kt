package com.example.qsdmovies

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class ForgotActivity : AppCompatActivity() {

    private lateinit var emailHere : EditText
    private lateinit var sendEmailButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        emailHere = findViewById(R.id.emailHere)
        sendEmailButton = findViewById(R.id.sendEmailButton)




    }
}
