package com.example.qsdmovies.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.R
import com.example.qsdmovies.databinding.ActivityLoginBinding
import com.example.qsdmovies.uitel.LoadingDialog
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    private val RC_SIGN_IN: Int = 1
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var loading: LoadingDialog

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        Log.d(TAG, "updateUI")
        if (currentUser != null) {
            startActivity(Intent(this, BottomBarActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        createRequest()

        createKeyHash(this, "com.example.qsdmovies")

        binding.facebookLogin.setOnClickListener {
            callbackManager = CallbackManager.Factory.create()
            binding.facebookLogin.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        Log.d(TAG, "facebook:onSuccess:$result")
                        handleFacebookAccessToken(result.accessToken)
                        val intent = Intent(this@LoginActivity, BottomBarActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onCancel() {
                        Log.d(TAG, "facebook:onCancel")
                    }

                    override fun onError(error: FacebookException) {
                        Log.d(TAG, "facebook:onError", error)
                    }
                })
        }

        binding.googleLogin.setOnClickListener {
            signIn()
        }

        binding.loginButton.setOnClickListener {
            login()
            val loading = LoadingDialog(this)
            loading.startLoading()
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    loading.isDismiss()
                }

            }, 5000)
        }

        binding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createRequest() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                    Toast.makeText(
                        baseContext, "signInWithCredential:success",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@LoginActivity, BottomBarActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }

    }

    private fun login() {
        val email = binding.emailHere.text.toString()
        val pass = binding.passwordHere.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "email field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (pass.isEmpty()) {
            Toast.makeText(this, "password field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.matches(emailPattern.toRegex())) {

        } else {
            Toast.makeText(
                applicationContext, "invalid email address",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (binding.passwordHere.text.toString().length < 8) {
            binding.passwordHere.error = "password minimum contain 8 character"
            binding.passwordHere.requestFocus()
            binding.passwordHere.isEnabled = true

        }
        if (binding.passwordHere.text.toString().length > 32) {
            binding.passwordHere.error = "password maximum contain 32 character"
            binding.passwordHere.requestFocus()
        }
        if (binding.passwordHere.text.toString().equals("")) {
            binding.passwordHere.error = "please enter password"
            binding.passwordHere.requestFocus()
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this, BottomBarActivity::class.java)
                startActivity(intent)
            } else
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            return@addOnCompleteListener
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                googleAuthForFirebase(account)
                val intent = Intent(this, BottomBarActivity::class.java)
                startActivity(intent)
            } catch (e: ApiException) {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credentials)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LoginActivity, BottomBarActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createKeyHash(activity: Activity, yourPackage: String) {
        val info =
            activity.packageManager.getPackageInfo(yourPackage, PackageManager.GET_SIGNATURES)
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }
    }
}


