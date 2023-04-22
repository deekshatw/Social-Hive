package com.example.socialhive

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import com.example.socialhive.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    // here we'll instantiate all variables
    private lateinit var binding: ActivityMainBinding

    // creating var to instantiate Firebase auth
    private lateinit var auth: FirebaseAuth
    // creating var to instantiate google sign in client
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initializing the auth variable
        auth = FirebaseAuth.getInstance()
        // we are creating a variable for Google Sign in options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // -> it may show error on default_web_client_id, it will disappear automatically
            .requestEmail()
            .build()

        // initializing google sign in client
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // adding on click listener to the google button
        binding.googleBtn.setOnClickListener {
            // also creating a separate method for google sign in
            googleSignIn()
        }

        // adding on click listener to create account button
        binding.createAccBtn.setOnClickListener {
            // making separate method for creating new account for user
            logInUserWithEmail()
        }

    }
    private fun logInUserWithEmail() {
        // getting the text from all the edit text fields in strings
        val name = binding.nameEt.text.toString()
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()
        val rePassword = binding.rePasswordEt.text.toString()

        // creating a var which will tell you if all the input fields are valid
        var isValid = false

        if (!isValid) { // checking if all are valid, if not showing the error message

            if (name.isEmpty()) {
                binding.nameEt.error = "Please enter your name"
                binding.nameEt.requestFocus()
            }
            if (email.isEmpty()) {
                binding.emailEt.error = "Please enter your email"
                binding.emailEt.requestFocus()
            }
            if (password.isEmpty()) {
                binding.passwordEt.error = "Please enter your password"
                binding.passwordEt.requestFocus()
            }
            if (name.isEmpty()) {
                binding.rePasswordEt.error = "Please enter your password again"
                binding.rePasswordEt.requestFocus()
            }
            if (password != rePassword) {
                binding.rePasswordEt.error = "Passwords don't match"
                binding.rePasswordEt.requestFocus()
            }
            isValid = true
        }
        if (isValid) { // if isValid true, then creating a new user
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    // storing the current user in user var
                    val user = auth.currentUser

                    // now signing in the user
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // adding intent if user is successfully created then pass the user to HomeActivity
                            val intent = Intent(this, HomeActivity::class.java)
                            // passing user's info through intent, so we can use that later
                            intent.putExtra("email", user?.email)
                            intent.putExtra("name", user?.displayName)
                            intent.putExtra("profile", user?.photoUrl)
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                this,
                                "Login error : ${it.exception.toString()}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Sign-up error : ${it.exception.toString()}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }

        }
    }

    private fun googleSignIn() {
        // creating a signInIntent which we can use later while signingIn
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    // this will get signed in account from the intent and handle the task in between
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    // this method will handle the task after the sign in with user is completed
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account!=null){
                updateUI(account)
            }

        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // this will update the UI according to the tasks
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent: Intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("gmail", account.email)
                intent.putExtra("gName", account.displayName)
                intent.putExtra("gProfile", account.photoUrl)
                startActivity(intent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }
}