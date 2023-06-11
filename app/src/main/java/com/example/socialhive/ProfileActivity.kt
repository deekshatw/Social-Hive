package com.example.socialhive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.socialhive.Adapter.PostAdapter
import com.example.socialhive.Model.PostModel
import com.example.socialhive.databinding.ActivityProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    // instantiating var for database reference
    private lateinit var dbReference: DatabaseReference

    // create some vars for adapter
    private lateinit var adapter: PostAdapter
    private lateinit var postsList: ArrayList<PostModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbReference = FirebaseDatabase.getInstance().getReference("Posts")
        postsList = arrayListOf<PostModel>()

        binding.profileRecyclerview.layoutManager = GridLayoutManager(this@ProfileActivity,
            2, GridLayoutManager.VERTICAL, false)
        binding.profileRecyclerview.setHasFixedSize(true)

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
                    binding.profileRecyclerview.adapter = adapter
                    binding.profileRecyclerview.scrollToPosition(0)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}