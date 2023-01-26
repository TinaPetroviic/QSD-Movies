package com.example.qsdmovies.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.qsdmovies.activity.AccountActivity
import com.example.qsdmovies.activity.ContactActivity
import com.example.qsdmovies.activity.LoginActivity
import com.example.qsdmovies.activity.WebViewHelpActivity
import com.example.qsdmovies.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadLocate()


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("User")

        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage!!.reference

        storageReference!!.child("myImages").child(auth!!.uid!!)
            .downloadUrl.addOnSuccessListener { uri ->
                this.context?.let {
                    Glide.with(it)
                        .load(uri)
                        .into(binding.profileImage)
                }
            }

        loadProfile()

        binding.account.setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }

        binding.contact.setOnClickListener {
            val intent = Intent(context, ContactActivity::class.java)
            startActivity(intent)
        }

        binding.language.setOnClickListener {
            showChangeLang()
        }

        binding.help.setOnClickListener {
            val intent = Intent(context, WebViewHelpActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            val mBuilder = this.context?.let { it1 ->
                AlertDialog.Builder(it1)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", null)
                    .setNegativeButton("No", null)
                    .show()
            }

            mBuilder?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@ProfileFragment.context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                mBuilder.dismiss()
            }
        }
    }

    private fun showChangeLang() {

        val listItmes = arrayOf("English", "Croatian", "German", "Spanish", "Italian")

        val mBuilder = this@ProfileFragment.context?.let { AlertDialog.Builder(it) }
        mBuilder?.setTitle("Choose Language")
        mBuilder?.setSingleChoiceItems(listItmes, -1) { dialog, which ->
            when (which) {
                0 -> {
                    setLocate("en")
                    activity?.recreate()
                }
                1 -> {
                    setLocate("hr")
                    activity?.recreate()
                }
                2 -> {
                    setLocate("de")
                    activity?.recreate()
                }
                3 -> {
                    setLocate("es")
                    activity?.recreate()
                }
                4 -> {
                    setLocate("it")
                    activity?.recreate()
                }
            }

            dialog.dismiss()
        }
        val mDialog = mBuilder?.create()

        mDialog?.show()

    }

    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        requireActivity().baseContext?.resources?.updateConfiguration(
            config,
            requireActivity().baseContext?.resources?.displayMetrics
        )

        val editor = activity?.getSharedPreferences("Settings", Context.MODE_PRIVATE)?.edit()
        editor?.putString("My_Lang", Lang)
        editor?.apply()
    }

    private fun loadLocate() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences?.getString("My_Lang", "")
        if (language != null) {
            setLocate(language)
        }
    }


    private fun loadProfile() {

        val user = auth?.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        Log.d("myDebugTag", "debug message")

        _binding?.firstName?.text = user?.displayName
        userreference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _binding?.firstName?.text = snapshot.child("firstName").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
