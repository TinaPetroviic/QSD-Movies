package com.example.qsdmovies.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.qsdmovies.R
import com.example.qsdmovies.data.User
import com.example.qsdmovies.databinding.ActivityAccountBinding
import com.example.qsdmovies.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private lateinit var database: DatabaseReference

    private var auth: FirebaseAuth? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var PICK_IMAGE_REQUEST = 12
    private var imagePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage!!.reference


        storageReference!!.child(Constants.IMAGE_PATH_DB).child(auth!!.uid!!)
            .downloadUrl.addOnSuccessListener { uri ->
                this.let {
                    Glide.with(it)
                        .load(uri)
                        .into(binding.imgProfilePhoto)
                }
            }

        binding.imgProfilePhoto.setOnClickListener {
            fileChooser()
        }

        binding.btnUpdate.setOnClickListener {

            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()

            updateData(firstName, lastName)

            sendData()
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

        val userProfile = User("firstName", "lastName")
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
                .into(binding.imgProfilePhoto)

        }
    }

    private fun updateData(firstName: String, lastName: String) {

        database = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH_DB).child(FirebaseAuth.getInstance().currentUser!!.uid)

        val user = mapOf(
            "firstName" to firstName,
            "lastName" to lastName
        )

        database.updateChildren(user).addOnSuccessListener {

            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            Toast.makeText(this, getString(R.string.successfully_updated), Toast.LENGTH_SHORT).show()


        }.addOnFailureListener {

            Toast.makeText(this, getString(R.string.failed_to_update), Toast.LENGTH_SHORT).show()

        }
    }
}