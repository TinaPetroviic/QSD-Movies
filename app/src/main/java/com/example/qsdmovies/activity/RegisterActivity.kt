package com.example.qsdmovies.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.R
import com.example.qsdmovies.data.User
import com.example.qsdmovies.databinding.ActivityRegisterBinding
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.hide
import com.example.qsdmovies.util.show
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    companion object {
        private const val PICK_IMAGE_REQUEST = 71
    }

    private var imagePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

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
        binding.ivProfile.setOnClickListener {
            launchGallery()
        }
        binding.tvAddProfilePicture.setOnClickListener {
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, receivedData: Intent?) {
        super.onActivityResult(requestCode, resultCode, receivedData)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (receivedData == null || receivedData.data == null) {
                return
            }

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, receivedData.data)
                binding.ivProfile.setImageBitmap(bitmap)
                receivedData.data?.let {
                    uploadImage(it)
                } ?: Timber.d(getString(R.string.receiveddata_uri_is_empty))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, getString(R.string.select_picture)),
            PICK_IMAGE_REQUEST
        )
    }

    private fun uploadImage(imageFilePath: Uri) {
        Timber.d("uploadImage")
        val imageRef = storageReference!!.child(FirebaseAuth.getInstance().uid!!)
            .child(Constants.IMAGE_PATH_DB)
        val uploadTask = imageRef.putFile(imageFilePath)
        uploadTask.addOnCompleteListener {
            if (it.isSuccessful) {
                Timber.d(getString(R.string.successfully_uploaded))
            } else {
                Timber.d(getString(R.string.failed_to_upload))
            }
        }
    }

    private fun signUpUser() {
        binding.viewLoading.root.show()
        Timber.d("signUpUser")
        val name = binding.etFirstName.text.toString()
        val surname = binding.etLastName.text.toString()
        val email = binding.etEmail.text.toString()
        val pass = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (name.length > Constants.FIRST_NAME_MINIMUM_LENGTH || name.length < Constants.FIRST_NAME_MAXIMUM_LENGTH) {
            binding.etPassword.error =
                "Password length must be between ${Constants.FIRST_NAME_MINIMUM_LENGTH} and ${Constants.FIRST_NAME_MAXIMUM_LENGTH} characters"
            binding.etPassword.requestFocus()
            binding.viewLoading.root.hide()
            return
        }

        if (surname.length > Constants.LAST_NAME_MINIMUM_LENGTH || surname.length < Constants.LAST_NAME_MAXIMUM_LENGTH) {
            binding.etPassword.error =
                "Password length must be between ${Constants.LAST_NAME_MINIMUM_LENGTH} and ${Constants.LAST_NAME_MAXIMUM_LENGTH} characters"
            binding.etPassword.requestFocus()
            binding.viewLoading.root.hide()
            return
        }

        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.email_field_cant_be_empty), Toast.LENGTH_SHORT)
                .show()
            binding.viewLoading.root.hide()
            return
        }

        if (pass.length > Constants.PASSWORD_MINIMUM_LENGTH || pass.length < Constants.PASSWORD_MAXIMUM_LENGTH) {
            binding.etPassword.error =
                "Password length must be between ${Constants.PASSWORD_MINIMUM_LENGTH} and ${Constants.PASSWORD_MAXIMUM_LENGTH} characters"
            binding.etPassword.requestFocus()
            binding.viewLoading.root.hide()
            return
        }

        if (confirmPassword.length > Constants.PASSWORD_MINIMUM_LENGTH || confirmPassword.length < Constants.PASSWORD_MAXIMUM_LENGTH) {
            binding.etPassword.error =
                "Confirm password length must be between ${Constants.PASSWORD_MINIMUM_LENGTH} and ${Constants.PASSWORD_MAXIMUM_LENGTH} characters"
            binding.etPassword.requestFocus()
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

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    saveData()
                    startActivity(Intent(this, BottomBarActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.user_already_exists),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.viewLoading.root.hide()
                }
            }
    }

    private fun saveData() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val user = User(firstName, lastName)

        database.child(Constants.USER_PATH_DB).child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user)
    }
}