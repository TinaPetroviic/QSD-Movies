package com.example.qsdmovies.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactBinding
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.sendEmailButton.setOnClickListener {

            val email = binding.email.text.toString().trim()
            val subject = binding.subject.text.toString().trim()
            val message = binding.message.text.toString().trim()

            sendEmail(email, subject, message)
        }
    }

    @SuppressLint("IntentReset")
    private fun sendEmail(email: String, subject: String, message: String) {

        val mIntent = Intent(Intent.ACTION_SEND)

        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"

        if (email.isEmpty()) {
            Toast.makeText(this, "email field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (subject.isEmpty()) {
            Toast.makeText(this, "subject field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (message.isEmpty()) {
            Toast.makeText(this, "message field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (email.matches(emailPattern.toRegex())) {

        } else {
            Toast.makeText(
                applicationContext, "invalid email address",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))

        } catch (e: Exception) {

            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()

        }
    }
}

