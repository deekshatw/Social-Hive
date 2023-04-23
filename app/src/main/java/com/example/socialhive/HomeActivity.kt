package com.example.socialhive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialhive.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeActivity : AppCompatActivity() {
// again instantiate these vars
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding

    // instantiating var for database reference
    private lateinit var dbReference: DatabaseReference


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

        // getting only the first name of the user
        val gNameParts = gName?.split(" ")
        val gFirstName = gNameParts?.get(0)

        // changing the text to Welcome "first name"
        binding.userName.text = "Welcome $gFirstName"

        // initializing dbReference, adding reference which will show in
        // db and we can use it later to retrieve data
        dbReference = FirebaseDatabase.getInstance().getReference("Users")

    }
}