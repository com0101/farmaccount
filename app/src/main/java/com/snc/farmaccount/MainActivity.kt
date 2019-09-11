package com.snc.farmaccount


import android.content.Context
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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.snc.farmaccount.databinding.FragmentLogInBinding


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
//    private lateinit var bindingLogin: FragmentLogInBinding
//    private lateinit var firebaseAuth: FirebaseAuth
//    val RC_SIGN_IN: Int = 1
//    lateinit var googleSignInClient: GoogleSignInClient
//    lateinit var googleSignInOptions: GoogleSignInOptions

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
//        firebaseAuth = FirebaseAuth.getInstance()
//        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)


    }

//    fun click() {
//        bindingLogin = FragmentLogInBinding.inflate(layoutInflater)
//        bindingLogin.signInButton.setOnClickListener {
//            Log.i("Sophie_click", "click")
//            signInGoogle()
//        }
//    }
//
//
//    private fun signInGoogle() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w("Sophie_fire_google", "Google sign in failed", e)
//                // ...
//            }
//        }
//    }
//
//    private fun updateUI(account: FirebaseUser?) {
////        binding.textView.text = account?.uid
//        bindingLogin = FragmentLogInBinding.inflate(layoutInflater)
//        bindingLogin.buttonLogOut.setOnClickListener {
//            googleSignInClient.signOut().addOnCompleteListener {
//                bindingLogin.textView.text = ""
//                Log.i("Sophie_click", "click")
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
//        Log.d("Sophie_fire_google", "firebaseAuthWithGoogle:" + account.id!!)
//
//        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//        firebaseAuth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("Sophie_fire_google", "signInWithCredential:success")
//                    val user = firebaseAuth.currentUser
//                    if (user != null) {
//                        updateUI(user)
//                        val usersToken = this.
//                            getSharedPreferences("Token", Context.MODE_PRIVATE)
//                        val editor = usersToken!!.edit()
//                        editor.putString("Token", user.uid ).apply()
//                        Log.i("Sophie_fire_google", "UID:" + user.uid)
//                    }
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("Sophie_fire_google", "signInWithCredential:failure", task.exception)
//                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
//                    updateUI(null)
//                }
//
//                // ...
//            }
//    }
//
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = firebaseAuth.currentUser
//        updateUI(currentUser)
//    }


}
