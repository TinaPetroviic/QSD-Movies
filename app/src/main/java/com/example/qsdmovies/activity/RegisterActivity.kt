package com.example.qsdmovies.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.data.User
import com.example.qsdmovies.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var database: DatabaseReference

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "BACK"

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        auth = Firebase.auth
        database = Firebase.database.reference

        binding.registerButton.setOnClickListener {
            signUpUser()
        }
        binding.addProfilePicture.setOnClickListener {
            launchGallery()
        }
        binding.profileImage.setOnClickListener {
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

        val name = binding.firstNameHere.text.toString()
        val surname = binding.lastNameHere.text.toString()
        val email = binding.emailRegister.text.toString()
        val pass = binding.passwordRegister.text.toString()
        val confirmPassword = binding.confirmPasswordRegister.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "first name field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (surname.isEmpty()) {
            Toast.makeText(this, "last name field can't be empty", Toast.LENGTH_SHORT).show()
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

        if (binding.passwordRegister.text.toString().length < 8) {
            binding.passwordRegister.setError("password minimum contain 8 character")
            binding.passwordRegister.requestFocus()
            binding.passwordRegister.isEnabled = true

        }
        if (binding.passwordRegister.text.toString().length > 32) {
            binding.passwordRegister.setError("password maximum contain 32 character")
            binding.passwordRegister.requestFocus()
        }
        if (binding.passwordRegister.text.toString().equals("")) {
            binding.passwordRegister.setError("please enter password")
            binding.passwordRegister.requestFocus()
        }

        if (binding.firstNameHere.text.toString().length < 1) {
            binding.firstNameHere.setError("first name minimum contain 1 character")
            binding.firstNameHere.requestFocus()
            binding.firstNameHere.isEnabled = true

        }
        if (binding.firstNameHere.text.toString().length > 50) {
            binding.firstNameHere.setError("first name maximum contain 50 character")
            binding.firstNameHere.requestFocus()
        }
        if (binding.firstNameHere.text.toString().equals("")) {
            binding.firstNameHere.setError("please enter first name")
            binding.firstNameHere.requestFocus()
        }

        if (binding.lastNameHere.text.toString().length < 1) {
            binding.lastNameHere.setError("last name minimum contain 1 character")
            binding.lastNameHere.requestFocus()
            binding.lastNameHere.isEnabled = true

        }
        if (binding.lastNameHere.text.toString().length > 50) {
            binding.lastNameHere.setError("last name maximum contain 50 character")
            binding.lastNameHere.requestFocus()
        }
        if (binding.lastNameHere.text.toString().equals("")) {
            binding.lastNameHere.setError("please enter last name")
            binding.lastNameHere.requestFocus()
        }


        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                saveData()

                val mProgressDialog = ProgressDialog(this)
                mProgressDialog.setMessage("Loading...")
                mProgressDialog.show()
                val intent = Intent(this, BottomBarActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "user already exists", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData() {

        firstName = binding.firstNameHere.text.toString().trim()
        lastName = binding.lastNameHere.text.toString().trim()

        val user = User(firstName, lastName, binding.profileImage)

        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("User").child(userID).setValue(user)

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
                binding.profileImage.setImageBitmap(bitmap)
                uploadImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}