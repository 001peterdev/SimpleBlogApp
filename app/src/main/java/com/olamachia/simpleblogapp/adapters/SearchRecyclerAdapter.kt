package com.olamachia.simpleblogapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.simpleblogapp.R
import com.olamachia.simpleblogapp.model.Post
import com.olamachia.simpleblogapp.mvvm.ui.fragments.AllPostsFragmentDirections
import com.olamachia.simpleblogapp.mvvm.ui.fragments.SearchFragmentDirections

class SearchRecyclerAdapter(var postList:List<Post>) : RecyclerView.Adapter<SearchRecyclerAdapter.SearchPostsViewHolder>() {
    private lateinit var postTitle: TextView
    private lateinit var postBody: TextView
    private lateinit var postUserId: TextView

    inner class SearchPostsViewHolder(itemView: View,var postSelected:Post?=null):RecyclerView.ViewHolder(itemView)  {

      init{
            postTitle = itemView.findViewById(R.id.post_title)
            postBody = itemView.findViewById(R.id.post_body)
            postUserId = itemView.findViewById(R.id.post_user_id)

          itemView.setOnClickListener {
              postSelected?.let{
                  val directions = SearchFragmentDirections.actionSearchFragmentToSinglePostFragment(it)
                  itemView.findNavController().navigate(directions)
              }
          }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchRecyclerAdapter.SearchPostsViewHolder {
        return SearchPostsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_post_item, parent, false))
    }



    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: SearchPostsViewHolder, position: Int) {
        val post = postList[position]
        val userid ="User: "+post.userId.toString()

        holder.itemView.apply {
            postTitle.text = post.title
            postBody.text = post.body
            postUserId.text = userid
        }
        holder.postSelected = post
    }
}