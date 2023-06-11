package com.example.socialhive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialhive.Model.PostModel
import com.example.socialhive.databinding.ActivityAddPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding

    // instantiating var for database reference
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbReference = FirebaseDatabase.getInstance().getReference("Posts")

        binding.postBtn.setOnClickListener {
            savePostData()
            binding.etPost.text.clear()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun savePostData() {
        // storing the text user entered in the edit text field in a separate variable
        val postDesc = binding.etPost.text.toString()
        val username = FirebaseAuth.getInstance().currentUser?.displayName

        // checking if post is empty
        if (postDesc.isEmpty()){
            binding.etPost.error = "Please add something in order to post!"
        }
        else{
            // generating a unique id for each post
            val postId = dbReference.push().key!!

            // storing all data related to each post in a var
            val post = PostModel(postId, username, postDesc)

            // adding child to the "Posts" reference which is the id of the post and add the contents under that child
            dbReference.child(postId).setValue(post)
                // adding onSuccess and onFailure listeners
                .addOnSuccessListener {
                    Toast.makeText(this, "Posted Successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AddPostActivity, HomeActivity::class.java))
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Error : ${error.message}", Toast.LENGTH_SHORT).show()
                }
        }


    }
}