package com.example.qsdmovies.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.qsdmovies.R
import com.example.qsdmovies.activity.*
import com.example.qsdmovies.databinding.FragmentProfileBinding
import com.example.qsdmovies.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var requestLauncher: ActivityResultLauncher<String>


    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {

                Toast.makeText(
                    requireContext(),
                    getString(R.string.notifications_are_allowed),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                showErrorMessage()
            }
        }

        loadLocate()


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child(Constants.USER_PATH_DB)

        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage!!.reference

        storageReference!!.child(Constants.IMAGE_PATH_DB).child(auth!!.uid!!)
            .downloadUrl.addOnSuccessListener { uri ->
                this.context?.let {
                    Glide.with(it)
                        .load(uri)
                        .into(binding.imgProfilePhoto)
                }
            }

        loadProfile()

        binding.llAccount.setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }

        binding.llNotification.setOnClickListener {

            askForNotificationPermission()

        }

        binding.llContact.setOnClickListener {
            val intent = Intent(context, ContactActivity::class.java)
            startActivity(intent)
        }

        binding.llLanguage.setOnClickListener {
            showChangeLang()
        }

        binding.llHelp.setOnClickListener {
            val intent = Intent(context, WebViewHelpActivity::class.java)
            startActivity(intent)
        }

        binding.llLogout.setOnClickListener {
            val mBuilder = this.context?.let { it1 ->
                AlertDialog.Builder(it1)
                    .setMessage(getString(R.string.Are_you_sure_you_want_to_exit))
                    .setPositiveButton(getString(R.string.yes), null)
                    .setNegativeButton(getString(R.string.no), null)
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

    private fun showRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.notifications_are_required_to_keep_you_updated_on_new_content))
            .setPositiveButton("Go to Settings") { dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", "com.example.qsdmovies", null)
                intent.data = uri
                startActivity(intent)
            }

            .create()
            .show()
    }

    private fun showErrorMessage() {
        showRationaleDialog()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askForNotificationPermission() {
        requestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }


    private fun showChangeLang() {

        val listItmes = arrayOf(
            getString(R.string.english), getString(R.string.croatian), getString(
                R.string.german
            ), getString(R.string.spanish), getString(R.string.italian)
        )

        val mBuilder = this@ProfileFragment.context?.let { AlertDialog.Builder(it) }
        mBuilder?.setTitle(getString(R.string.choose_language))
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
        val userReference = databaseReference?.child(user?.uid!!)


        binding.tvFirstName.text = user?.displayName
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.tvFirstName.text = snapshot.child(Constants.FIRST_NAME_DB).value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
