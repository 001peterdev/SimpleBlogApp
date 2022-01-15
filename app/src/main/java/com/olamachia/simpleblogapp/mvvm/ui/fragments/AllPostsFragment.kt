package com.olamachia.simpleblogapp.mvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.olamachia.simpleblogapp.R
import com.olamachia.simpleblogapp.adapters.AllPostsRecyclerAdapter
import com.olamachia.simpleblogapp.mvvm.ui.BlogPostViewModel
import com.olamachia.simpleblogapp.mvvm.ui.MVVMActivity
import com.olamachia.simpleblogapp.utils.Resource


class AllPostsFragment : Fragment() {

    // Create Lateinit variables for components
    private lateinit var viewModel: BlogPostViewModel
    private lateinit var allPostsRecyclerAdapter: AllPostsRecyclerAdapter
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postProgressBar: ProgressBar
    private lateinit var addPost :FloatingActionButton
    private lateinit var searchPost :ImageView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize the views declared as lateinit
        postRecyclerView = view.findViewById(R.id.all_posts_rv)
        postProgressBar = view.findViewById(R.id.post_progressbar)
        addPost = view.findViewById(R.id.add_Post_fab)
        searchPost = view.findViewById(R.id.search_post_btn)

        //Initialize view model
        viewModel = (activity as MVVMActivity).viewModel

        // Set the on click listener that navigates to the add post fragment
        addPost.setOnClickListener {
                val directions = AllPostsFragmentDirections.actionAllPostsFragmentToAddPostFragment()
                addPost.findNavController().navigate(directions)
        }

        // Set the on click listener that navigates to the search post fragment
        searchPost.setOnClickListener {
            val direction = AllPostsFragmentDirections.actionAllPostsFragmentToSearchFragment()
            searchPost.findNavController().navigate(direction)
        }

        setupRecyclerView()

        //Observe the Blog posts live data in the viewmodel
        viewModel.blogPosts.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    response.data?.let{post ->
                        postProgressBar.visibility= View.INVISIBLE
                        // submit the fetched posts to the differ in the adapter
                        allPostsRecyclerAdapter.differ.submitList(post)
                    Log.d("All Posts Fragment", "Posts:${post.size}")

                    }
                }
                is Resource.Error->{
                    response.message?.let { message->
                        postProgressBar.visibility= View.INVISIBLE
                        Log.d("All Posts Fragment", "Fetch error:$message")
                    }
                }
                is Resource.Loading -> {
                    //show the user the progress bar so they know the call is being made
                    postProgressBar.visibility= View.VISIBLE
//                    Toast.makeText(activity,"Loading", Toast.LENGTH_LONG).show()
                }
            }
        })


    }

    private fun setupRecyclerView(){
        allPostsRecyclerAdapter = AllPostsRecyclerAdapter()
        postRecyclerView.apply {
            adapter = allPostsRecyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
