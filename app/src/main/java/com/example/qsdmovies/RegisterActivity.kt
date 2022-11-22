package com.example.qsdmovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var firstNameHere: EditText
    private lateinit var lastNameHere: EditText
    private lateinit var emailRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var confirmPasswordRegister: EditText
    private lateinit var registerButton : Button

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val actionbar = supportActionBar
        actionbar!!.title = "BACK"

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)


        firstNameHere = findViewById(R.id.firstNameHere)
        lastNameHere = findViewById(R.id.lastNameHere)
        emailRegister = findViewById(R.id.emailRegister)
        passwordRegister = findViewById(R.id.passwordRegister)
        confirmPasswordRegister = findViewById(R.id.confirmPasswordRegister)
        registerButton = findViewById(R.id.registerButton)

        auth = Firebase.auth

        registerButton.setOnClickListener {
            signUpUser()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun signUpUser() {
        val name = firstNameHere.text.toString()
        val surname = lastNameHere.text.toString()
        val email = emailRegister.text.toString()
        val pass = passwordRegister.text.toString()
        val confirmPassword = confirmPasswordRegister.text.toString()

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "Successfully Singed Up", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}