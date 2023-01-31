package com.example.qsdmovies.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.R
import com.example.qsdmovies.databinding.ActivityLoginBinding
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.hide
import com.example.qsdmovies.util.show
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
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private var backPressedTime = 0L

    val TAG = "LoginActivity"

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    private val RC_SIGN_IN: Int = 1
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    public override fun onStart() {
        super.onStart()
        Timber.tag(TAG).d("onStart")
        updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        Timber.tag(TAG).d("updateUI")
        if (currentUser != null) {
            startActivity(Intent(this, BottomBarActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        createRequest()

        binding.facebookLogin.setOnClickListener {
            binding.viewLoading.root.show()
            callbackManager = CallbackManager.Factory.create()
            binding.facebookLogin.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        Timber.tag(TAG).d("facebook:onSuccess:%s", result)
                        handleFacebookAccessToken(result.accessToken)
                    }

                    override fun onCancel() {
                        binding.viewLoading.root.hide()
                        Timber.tag(TAG).d("facebook:onCancel")
                    }

                    override fun onError(error: FacebookException) {
                        binding.viewLoading.root.hide()
                        Timber.tag(TAG).d(error, "facebook:onError")
                    }
                })
        }

        binding.imgGoogleLogin.setOnClickListener {
            signIn()
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(
                applicationContext,
                getString(R.string.press_back_again_to_exit_app),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        backPressedTime = System.currentTimeMillis()
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

        Timber.tag(TAG).d("handleFacebookAccessToken:%s", token)

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Timber.tag(TAG).d("signInWithCredential:success")
                    updateUI(auth.currentUser)
                    Toast.makeText(
                        baseContext, "signInWithCredential:success",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Timber.tag(TAG).w(task.exception, "signInWithCredential:failure")
                    Toast.makeText(
                        baseContext, getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                    binding.viewLoading.root.hide()
                }
            }

    }

    private fun login() {
        binding.viewLoading.root.show()

        val email = binding.etEmail.text.toString()
        val pass = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.email_field_cant_be_empty), Toast.LENGTH_SHORT)
                .show()
            binding.viewLoading.root.hide()
            return
        }
        if (pass.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.password_field_cant_be_empty),
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
        if (binding.etPassword.text.toString().length < Constants.PASSWORD_MINIMUM_LENGTH) {
            binding.etPassword.error =
                "password minimum contain ${Constants.PASSWORD_MINIMUM_LENGTH} character"
            binding.etPassword.requestFocus()
            binding.etPassword.isEnabled = true
            binding.viewLoading.root.hide()
            return
        }
        if (binding.etPassword.text.toString().length > Constants.PASSWORD_MAXIMUM_LENGTH) {
            binding.etPassword.error =
                "password maximum contain ${Constants.PASSWORD_MAXIMUM_LENGTH} character"
            binding.etPassword.requestFocus()
            binding.viewLoading.root.hide()
            return
        }
        if (binding.etPassword.text.toString() == "") {
            binding.etPassword.error = getString(R.string.please_enter_password)
            binding.etPassword.requestFocus()
            binding.viewLoading.root.hide()
            return
        }


        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this, BottomBarActivity::class.java)
                startActivity(intent)
            } else
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            binding.viewLoading.root.hide()
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
            } catch (e: ApiException) {
                binding.viewLoading.root.hide()
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT)
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
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.viewLoading.root.hide()
                }
            }.addOnFailureListener {
                binding.viewLoading.root.hide()
            }
    }
}


