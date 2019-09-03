package com.snc.farmaccount.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.MainActivity

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentLogInBinding


class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val RC_SIGN_IN: Int = 1
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var googleSignInOptions: GoogleSignInOptions

    private val viewModel: LogInViewModel by lazy {
        ViewModelProviders.of(this).get(LogInViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        firebaseAuth = FirebaseAuth.getInstance()
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(ApplicationContext.applicationContext(), googleSignInOptions)
        navigationToHome()
        logIn()

        return binding.root
    }

    private fun logIn() {
        binding.signInButton.setOnClickListener {
            Log.i("Sophie_click", "click")
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
        binding.buttonLogOut.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                Log.i("Sophie_click", "click")
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d("Sophie_fire_google", "firebaseAuthWithGoogle:" + account.id!!)

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this.activity as MainActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sophie_fire_google", "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        updateUI(user)
                        val usersToken = this.context?.
                            getSharedPreferences("Token", Context.MODE_PRIVATE)
                        val editor = usersToken!!.edit()
                        editor.putString("Token", user.uid ).apply()
                        Log.i("Sophie_fire_google", "UID:" + user.uid)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sophie_fire_google", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this.context, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

            }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        updateUI(currentUser)
    }

    private fun navigationToHome() {
        if (viewModel.checkLogIn.value!!) {
           findNavController()
               .navigate(R.id.action_global_homeFragment)
        }

    }

}
