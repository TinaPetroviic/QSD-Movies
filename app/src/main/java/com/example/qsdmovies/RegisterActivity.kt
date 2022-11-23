package com.example.qsdmovies

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class RegisterActivity : AppCompatActivity() {
    private lateinit var firstNameHere: EditText
    private lateinit var lastNameHere: EditText
    private lateinit var emailRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var confirmPasswordRegister: EditText
    private lateinit var registerButton : Button
    private lateinit var imageProfilePicture : ImageView
    private lateinit var addProfilePicture : TextView

    private  var storageRef = Firebase.storage

    private lateinit var uri: Uri
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        storageRef = FirebaseStorage.getInstance()

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
        imageProfilePicture = findViewById(R.id.imageProfilePicture)
        addProfilePicture = findViewById(R.id.addProfilePicture)

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageProfilePicture.setImageURI(it)
                uri = it
            })

        addProfilePicture.setOnClickListener {
            galleryImage.launch("image/*")
            storageRef.getReference("images").child(System.currentTimeMillis().toString())
                .putFile(uri)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {
                            val userId = FirebaseAuth.getInstance().currentUser!!.uid
                            val mapImage = mapOf(
                                "url" to it.toString()
                            )

                            val databaseReference = FirebaseDatabase.getInstance().getReference("user Images")
                            databaseReference.child(userId).setValue(mapImage)
                                .addOnSuccessListener {
                                    Toast.makeText(this,"Successful", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                                }
                        }
                }
        }


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