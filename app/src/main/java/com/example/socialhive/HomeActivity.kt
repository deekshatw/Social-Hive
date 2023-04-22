package com.example.socialhive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialhive.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
// again instantiate these vars
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initializing auth
        auth = FirebaseAuth.getInstance()

        // getting data from the intents passed
        val email = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")
        val profile = intent.getStringExtra("profile")
        val gName = intent.getStringExtra("gName")
        val gmail = intent.getStringExtra("gmail")
        val gProfile = intent.getStringExtra("gProfile")

        // changing the text to show the name of user got from the google account
        binding.textView.text = gName
    }
}