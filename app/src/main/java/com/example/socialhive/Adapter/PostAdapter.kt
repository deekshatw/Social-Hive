package com.example.socialhive.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialhive.Model.PostModel
import com.example.socialhive.R

class PostAdapter(val postsLIst : ArrayList<PostModel>)
    : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // will inflate the layout of post_items.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_items, parent, false)
        return PostViewHolder(itemView)
    }

    // return total no of items
    override fun getItemCount(): Int {
        return postsLIst.size
    }

    // will bind the views to view holder
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = postsLIst[position]
        holder.username.text = currentPost.username
        holder.postDesc.text = currentPost.postDesc
    }

    // View Holder class which will hold the views
    class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.usernameTv)
        val postDesc = itemView.findViewById<TextView>(R.id.postDescTv)
    }


}