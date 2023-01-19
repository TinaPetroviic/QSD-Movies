package com.example.qsdmovies.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityContactBinding
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendEmailButton.setOnClickListener {

            val enterEmail = enter_email.text.toString().trim()
            val enterSubjectDetails = enter_subject_details.text.toString().trim()
            val message = message.text.toString().trim()

            sendEmail(enterEmail, enterSubjectDetails, message)
        }

    }

    private fun sendEmail(enterEmail: String, enterSubjectDetails: String, message: String) {

        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("qsd.testing@gmail.com")
        mIntent.type = "text/plain"

        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(enterEmail))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, arrayOf(enterSubjectDetails))
        mIntent.putExtra(Intent.EXTRA_TEXT, arrayOf(message))

        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }


    }
}