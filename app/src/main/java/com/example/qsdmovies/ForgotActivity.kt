package com.example.qsdmovies

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class ForgotActivity : AppCompatActivity() {

    private lateinit var emailForgot: EditText
    private lateinit var sendEmailButton: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        emailForgot = findViewById(R.id.emailForgot)
        sendEmailButton = findViewById(R.id.sendEmailButton)

        auth = FirebaseAuth.getInstance()

        sendEmailButton.setOnClickListener {
            val email = emailForgot.text.toString()
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(this,"Please Check your Email",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }

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
