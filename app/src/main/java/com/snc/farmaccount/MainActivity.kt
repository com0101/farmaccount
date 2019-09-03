package com.snc.farmaccount


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.*
import com.google.firebase.auth.FirebaseAuth
import com.snc.farmaccount.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val RC_SIGN_IN: Int = 1
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var googleSignInOptions: GoogleSignInOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        firebaseAuth = FirebaseAuth.getInstance()
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        binding.signInButton.setOnClickListener {
            signInGoogle()
        }

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Sophie_fire_google", "Google sign in failed", e)
                // ...
            }
        }
    }


    private fun updateUI(account: FirebaseUser?) {
        binding.textView.text = account?.displayName
        binding.buttonLogOut.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                binding.textView.text = ""
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d("Sophie_fire_google", "firebaseAuthWithGoogle:" + account.id!!)

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sophie_fire_google", "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        updateUI(user)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sophie_fire_google", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        updateUI(currentUser)
    }
    
}
