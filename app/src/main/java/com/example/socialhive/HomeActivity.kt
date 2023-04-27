package com.example.socialhive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialhive.Adapter.PostAdapter
import com.example.socialhive.Model.PostModel
import com.example.socialhive.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.logging.Formatter

class HomeActivity : AppCompatActivity() {
// again instantiate these vars
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding

    // instantiating var for database reference
    private lateinit var dbReference: DatabaseReference
    private lateinit var postRef : DatabaseReference

    // create some vars for adapter
    private lateinit var adapter : PostAdapter
    private lateinit var postsList : ArrayList<PostModel>

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
        dbReference = FirebaseDatabase.getInstance().getReference("Users/$gName")
        // now we need to make a child in the database reference "Users" and store the new reference
        // inside a new var postRef, which will give reference directly to the posts
        postRef = dbReference.child("posts")
        binding.sendPost.setOnClickListener {
            savePostData()
        }

        postsList = arrayListOf<PostModel>()
        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewPosts.setHasFixedSize(true)

        fetchPostsData()
    }


    private fun savePostData() {
        // storing the text user entered in the edit text field in a separate variable
        val postDesc = binding.etAddPost.text.toString()
        val username = intent.getStringExtra("gName")

        // checking if post is empty
        if (postDesc.isEmpty()){
            binding.etAddPost.error = "Please add something in order to post!"
        }

        // generating a unique id for each post
        val postId = postRef.push().key!!

        // storing all data related to each post in a var
        val post = PostModel(postId, username, postDesc)

        // adding child to the "posts" reference which is the id of the post and add the contents under that child
        postRef.child(postId).setValue(post)
                // adding onSuccess and onFailure listeners
            .addOnSuccessListener {
                Toast.makeText(this, "Posted Successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error : ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun fetchPostsData() {
        postRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (postSnapshot in snapshot.children){
                        val postData = postSnapshot.getValue(PostModel::class.java)
                        postsList.add(postData!!)
                    }
                    adapter = PostAdapter(postsList)
                    binding.recyclerViewPosts.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // nothing to do
            }
        })
    }
}