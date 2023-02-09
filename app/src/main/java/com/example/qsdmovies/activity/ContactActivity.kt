package com.example.qsdmovies.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.R
import com.example.qsdmovies.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSendEmail.setOnClickListener {

            val subject = binding.etSubject.text.toString().trim()
            val message = binding.etMessage.text.toString().trim()

            sendEmail(subject, message)

        }
    }

    @SuppressLint("IntentReset")
    private fun sendEmail(subject: String, message: String) {

        val mIntent = Intent(Intent.ACTION_SENDTO)

        mIntent.data = Uri.parse("mailto:test@mail.com")


        if (subject.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.subject_field_cant_be_empty),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (message.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.message_field_cant_be_empty),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(mIntent, getString(R.string.choose_email_client)))

        } catch (e: Exception) {

            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()

        }
    }
}

