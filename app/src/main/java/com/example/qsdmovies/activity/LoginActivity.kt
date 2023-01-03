package com.example.qsdmovies.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.qsdmovies.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
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

    private lateinit var emailHere: EditText
    private lateinit var passwordHere: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPassword: TextView
    private lateinit var register: TextView
    private lateinit var googleLogin: ImageView
    private lateinit var facebookLogin: LoginButton

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 1
    private lateinit var gso: GoogleSignInOptions

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.commit()
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        Log.d(TAG, "updateUI")
        if (currentUser != null) {
            startActivity(Intent(this, BottomBarActivity::class.java))
            //replaceFragment(HomeFragment())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailHere = findViewById(R.id.emailHere)
        passwordHere = findViewById(R.id.passwordHere)
        loginButton = findViewById(R.id.loginButton)
        forgotPassword = findViewById(R.id.forgotPassword)
        register = findViewById(R.id.register)
        googleLogin = findViewById(R.id.googleLogin)
        facebookLogin = findViewById(R.id.facebookLogin)

        auth = Firebase.auth

        createRequest()

        createKeyHash(this, "com.example.qsdmovies")

        facebookLogin.setOnClickListener {
            callbackManager = CallbackManager.Factory.create()
            facebookLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
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

        googleLogin.setOnClickListener {
            signIn()
        }

        loginButton.setOnClickListener {
            login()
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        forgotPassword.setOnClickListener {
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

        val email = emailHere.text.toString()
        val pass = passwordHere.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.matches(emailPattern.toRegex())) {

        } else {
            Toast.makeText(
                applicationContext, "Invalid email address",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (passwordHere.text.toString().length < 8) {
            passwordHere.setError("password minimum contain 8 character")
            passwordHere.requestFocus()
            passwordHere.isEnabled = true

        }
        if (passwordHere.text.toString().length > 8) {
            passwordHere.setError("password maximum contain 8 character")
            passwordHere.requestFocus()
        }
        if (passwordHere.text.toString().equals("")) {
            passwordHere.setError("please enter password")
            passwordHere.requestFocus()
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this, BottomBarActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
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
                    val user = auth.currentUser
                    val intent = Intent(this@LoginActivity, BottomBarActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, "Login Failed: ", Toast.LENGTH_SHORT).show()
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



