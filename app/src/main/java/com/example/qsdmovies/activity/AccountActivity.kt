package com.example.qsdmovies.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.qsdmovies.databinding.ActivityAccountBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.updateButton.setOnClickListener {

            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()

            updateData(firstName, lastName)
        }
    }

    private fun updateData(firstName: String, lastName: String) {

        database = FirebaseDatabase.getInstance().getReference("User")
        val user = mapOf<String, String>(
            "firstName" to firstName,
            "lastName" to lastName
        )

        database.child(firstName).updateChildren(user).addOnSuccessListener {

            binding.firstName.text.clear()
            binding.lastName.text.clear()
            Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {

            Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()

        }
    }
}