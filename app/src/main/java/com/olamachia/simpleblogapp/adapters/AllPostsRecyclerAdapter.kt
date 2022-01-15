package com.olamachia.simpleblogapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.simpleblogapp.R
import com.olamachia.simpleblogapp.model.Post
import com.olamachia.simpleblogapp.mvvm.ui.fragments.AllPostsFragmentDirections

class AllPostsRecyclerAdapter : RecyclerView.Adapter<AllPostsRecyclerAdapter.AllPostsViewHolder>(){

    private lateinit var postTitle:TextView
    private lateinit var postBody:TextView
    private lateinit var postUserId:TextView

    inner class AllPostsViewHolder(itemView: View, var post:Post?=null):RecyclerView.ViewHolder(itemView) {
        init {
            postTitle = itemView.findViewById(R.id.post_title)
            postBody = itemView.findViewById(R.id.post_body)
            postUserId = itemView.findViewById(R.id.post_user_id)

            itemView.setOnClickListener {
                post?.let{
                    val directions = AllPostsFragmentDirections.actionAllPostsFragmentToSinglePostFragment(it)
                    itemView.findNavController().navigate(directions)
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPostsViewHolder {
        return AllPostsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_post_item,parent,false))
    }

    override fun onBindViewHolder(holder: AllPostsViewHolder, position: Int) {
        val selectedPost = differ.currentList[position]
        val userid ="User: "+selectedPost.userId.toString()

          holder.itemView.apply {
              postTitle.text = selectedPost.title
              postBody.text = selectedPost.body
              postUserId.text = userid
          }
       holder.post = selectedPost
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}