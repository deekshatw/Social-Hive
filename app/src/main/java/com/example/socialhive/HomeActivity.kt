package com.example.socialhive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialhive.Adapter.PostAdapter
import com.example.socialhive.Model.PostModel
import com.example.socialhive.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {
    // again instantiate these vars
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding

    // instantiating var for database reference
    private lateinit var dbReference: DatabaseReference

    // create some vars for adapter
    private lateinit var adapter: PostAdapter
    private lateinit var postsList: ArrayList<PostModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initializing auth
        auth = FirebaseAuth.getInstance()

        val gName = FirebaseAuth.getInstance().currentUser?.displayName

        // getting only the first name of the user
        val gNameParts = gName?.split(" ")
        val gFirstName = gNameParts?.get(0)

        // changing the text to Welcome "first name"
        binding.userName.text = "Welcome $gFirstName"

        dbReference = FirebaseDatabase.getInstance().getReference("Posts")

        binding.addPostBtn.setOnClickListener {
            startActivity(Intent(this@HomeActivity, AddPostActivity::class.java))
        }

        binding.profileCardview.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
        }
        binding.profileImageview.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
        }

        postsList = arrayListOf<PostModel>()
        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewPosts.setHasFixedSize(true)

        fetchPostsData()
    }


    private fun fetchPostsData() {
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (postSnapshot in snapshot.children) {
                        val postData = postSnapshot.getValue(PostModel::class.java)
                        postsList.add(postData!!)
                    }
                    postsList.reverse()
                    adapter = PostAdapter(postsList)
                    binding.recyclerViewPosts.adapter = adapter
                    binding.recyclerViewPosts.scrollToPosition(0)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}