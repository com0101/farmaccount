package com.snc.farmaccount.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.MainActivity
import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentLogInBinding
import com.snc.farmaccount.helper.UserManager
import kotlinx.coroutines.*


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
        logIn()

        navigationToHome()
        return binding.root
    }

    private fun logIn() {
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
                Log.w("Sophie_fire_google", "Google sign in failed", e)
            }

        }
    }
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d("Sophie_fire_google", "firebaseAuthWithGoogle:" + account.id!!)
        var firebaseAuth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sophie_fire_google", "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        val usersToken = ApplicationContext.applicationContext()
                            .getSharedPreferences("Token", Context.MODE_PRIVATE)
                        val editor = usersToken!!.edit()
                        editor.putString("Token", user.uid ).apply()
                        editor.putString("Name", user.displayName ).apply()
                        editor.putString("email", user.email ).apply()
                        viewModel.getProfile()
                        checkUserStatus()
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sophie_fire_google", "signInWithCredential:failure", task.exception)
                }

            }
    }



    private fun navigationToHome() {
        if (viewModel.checkLogIn.value == true) {
            findNavController()
                .navigate(R.id.action_global_homeFragment)
        }
    }

    private fun checkUserStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            findNavController()
                .navigate(R.id.action_global_loadingFragment)
            delay(2500)
            if(viewModel.checkFirst.value == true) {
                findNavController()
                    .navigate(R.id.action_global_chooseFragment)
            } else {
                findNavController()
                    .navigate(R.id.action_global_homeFragment)
            }
        }

    }


}
