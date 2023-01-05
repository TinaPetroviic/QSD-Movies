package com.example.qsdmovies.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var firstNameHere: EditText
    private lateinit var lastNameHere: EditText
    private lateinit var emailRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var confirmPasswordRegister: EditText
    private lateinit var registerButton: Button
    private lateinit var profileImage: CircleImageView
    private lateinit var addProfilePicture: TextView

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"


    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "BACK"

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        firstNameHere = findViewById(R.id.firstNameHere)
        lastNameHere = findViewById(R.id.lastNameHere)
        emailRegister = findViewById(R.id.emailRegister)
        passwordRegister = findViewById(R.id.passwordRegister)
        confirmPasswordRegister = findViewById(R.id.confirmPasswordRegister)
        registerButton = findViewById(R.id.registerButton)
        profileImage = findViewById(R.id.profileImage)
        addProfilePicture = findViewById(R.id.addProfilePicture)


        auth = Firebase.auth

        registerButton.setOnClickListener {
            signUpUser()
        }
        addProfilePicture.setOnClickListener {
            launchGallery()
        }
        profileImage.setOnClickListener {
            launchGallery()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            super.onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage() {
        Log.d("RegisterActivity", "uploadImage")
        if (filePath != null) {
            val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)
            uploadTask?.addOnCompleteListener {
                if (it.isSuccessful) {

                }
            }

        } else {
            Log.d("RegisterActivity", "filepath null")
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun signUpUser() {

        val name = firstNameHere.text.toString()
        val surname = lastNameHere.text.toString()
        val email = emailRegister.text.toString()
        val pass = passwordRegister.text.toString()
        val confirmPassword = confirmPasswordRegister.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "name field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (surname.isEmpty()) {
            Toast.makeText(this, "email field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "email field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass.isEmpty()) {
            Toast.makeText(this, "password field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "confirm password field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(this, "password and confirm password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (email.matches(emailPattern.toRegex())) {

        } else {
            Toast.makeText(
                applicationContext, "invalid email address",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (passwordRegister.text.toString().length < 8) {
            passwordRegister.setError("password minimum contain 8 character")
            passwordRegister.requestFocus()
            passwordRegister.isEnabled = true

        }
        if (passwordRegister.text.toString().length > 32) {
            passwordRegister.setError("password maximum contain 32 character")
            passwordRegister.requestFocus()
        }
        if (passwordRegister.text.toString().equals("")) {
            passwordRegister.setError("please enter password")
            passwordRegister.requestFocus()
        }

        if (firstNameHere.text.toString().length < 1) {
            firstNameHere.setError("first name minimum contain 1 character")
            firstNameHere.requestFocus()
            firstNameHere.isEnabled = true

        }
        if (firstNameHere.text.toString().length > 50) {
            firstNameHere.setError("first name maximum contain 50 character")
            firstNameHere.requestFocus()
        }
        if (firstNameHere.text.toString().equals("")) {
            firstNameHere.setError("please enter first name")
            firstNameHere.requestFocus()
        }

        if (lastNameHere.text.toString().length < 1) {
            lastNameHere.setError("last name minimum contain 1 character")
            lastNameHere.requestFocus()
            lastNameHere.isEnabled = true

        }
        if (lastNameHere.text.toString().length > 50) {
            lastNameHere.setError("last name maximum contain 50 character")
            lastNameHere.requestFocus()
        }
        if (lastNameHere.text.toString().equals("")) {
            lastNameHere.setError("please enter last name")
            lastNameHere.requestFocus()
        }


        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this, BottomBarActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "Successfully Singed Up", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                profileImage.setImageBitmap(bitmap)
                uploadImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}