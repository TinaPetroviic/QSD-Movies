package com.example.qsdmovies.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.qsdmovies.R
import com.example.qsdmovies.data.User
import com.example.qsdmovies.databinding.ActivityRegisterBinding
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.hide
import com.example.qsdmovies.util.show
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private var PICK_IMAGE_REQUEST = 12
    private var imagePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var database: DatabaseReference

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        auth = Firebase.auth
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        database = Firebase.database.reference

        binding.btnRegister.setOnClickListener {
            signUpUser()
        }
        binding.tvAddProfilePicture.setOnClickListener {
            fileChooser()
        }
        binding.ivProfile.setOnClickListener {
            fileChooser()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            super.onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun signUpUser() {

        binding.viewLoading.root.show()

        Timber.d("signUpUser")

        val name = binding.etFirstName.text.toString()
        val surname = binding.etLastName.text.toString()
        val email = binding.etEmail.text.toString()
        val pass = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (name.length < Constants.FIRST_NAME_MINIMUM_LENGTH || name.length > Constants.FIRST_NAME_MAXIMUM_LENGTH) {
            binding.etFirstName.error =
                "Password length must be between ${Constants.FIRST_NAME_MINIMUM_LENGTH} and ${Constants.FIRST_NAME_MAXIMUM_LENGTH} characters"
            binding.etFirstName.requestFocus()
            binding.viewLoading.root.hide()
            return
        }

        if (surname.length < Constants.LAST_NAME_MINIMUM_LENGTH || surname.length > Constants.LAST_NAME_MAXIMUM_LENGTH) {
            binding.etLastName.error =
                "Password length must be between ${Constants.LAST_NAME_MINIMUM_LENGTH} and ${Constants.LAST_NAME_MAXIMUM_LENGTH} characters"
            binding.etLastName.requestFocus()
            binding.viewLoading.root.hide()
            return
        }

        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.email_field_cant_be_empty), Toast.LENGTH_SHORT)
                .show()
            binding.viewLoading.root.hide()
            return
        }

        if (pass.length < Constants.PASSWORD_MINIMUM_LENGTH || pass.length > Constants.PASSWORD_MAXIMUM_LENGTH) {
            binding.etPassword.error =
                "Password length must be between ${Constants.PASSWORD_MINIMUM_LENGTH} and ${Constants.PASSWORD_MAXIMUM_LENGTH} characters"
            binding.etPassword.requestFocus()
            binding.viewLoading.root.hide()
            return
        }

        if (confirmPassword.length < Constants.PASSWORD_MINIMUM_LENGTH || confirmPassword.length > Constants.PASSWORD_MAXIMUM_LENGTH) {
            binding.etConfirmPassword.error =
                "Confirm password length must be between ${Constants.PASSWORD_MINIMUM_LENGTH} and ${Constants.PASSWORD_MAXIMUM_LENGTH} characters"
            binding.etConfirmPassword.requestFocus()
            binding.viewLoading.root.hide()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(
                this,
                getString(R.string.confirm_password_do_not_match),
                Toast.LENGTH_SHORT
            ).show()
            binding.viewLoading.root.hide()
            return
        }

        if (!email.matches(Constants.EMAIL_PATTERN.toRegex())) {
            Toast.makeText(
                applicationContext,
                getString(R.string.invalid_email_address),
                Toast.LENGTH_SHORT
            ).show()
            binding.viewLoading.root.hide()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                saveData()
                sendData()
                val intent = Intent(this, BottomBarActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.user_already_exists), Toast.LENGTH_SHORT)
                    .show()
                return@addOnCompleteListener
            }
        }
    }

    private fun sendData() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val myReference = firebaseDatabase.getReference(auth?.uid.toString())

        val imageRef = storageReference!!.child(Constants.IMAGE_PATH_DB).child(auth!!.uid!!)
        val uploadImage = imageRef.putFile(imagePath!!)
        uploadImage.addOnFailureListener {
            Toast.makeText(this, getString(R.string.error_ocoured), Toast.LENGTH_SHORT).show()
        }

        val userProfile = User(firstName, lastName)
        myReference.setValue(userProfile)
    }

    private fun fileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            imagePath = data.data
            Glide.with(this)
                .load(imagePath)
                .into(binding.ivProfile)

        }
    }

    private fun saveData() {

        firstName = binding.etFirstName.text.toString().trim()
        lastName = binding.etLastName.text.toString().trim()

        val user = User(firstName, lastName)

        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        database.child(Constants.USER_PATH_DB).child(userID).setValue(user)

    }
}