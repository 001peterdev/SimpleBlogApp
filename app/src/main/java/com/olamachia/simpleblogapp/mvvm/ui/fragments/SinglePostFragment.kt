package com.olamachia.simpleblogapp.mvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.simpleblogapp.R
import com.olamachia.simpleblogapp.adapters.CommentsRecyclerAdapter
import com.olamachia.simpleblogapp.model.Post
import com.olamachia.simpleblogapp.mvvm.ui.BlogPostViewModel
import com.olamachia.simpleblogapp.mvvm.ui.MVVMActivity
import com.olamachia.simpleblogapp.utils.Resource


class SinglePostFragment : Fragment() {

    // Create Lateinit variables for components
    private val args: SinglePostFragmentArgs by navArgs()
    private lateinit var viewModel: BlogPostViewModel
    private lateinit var commentsRecyclerAdapter:CommentsRecyclerAdapter
    private lateinit var commentsRecyclerView:RecyclerView
    private lateinit var commentProgressBar:ProgressBar
    private lateinit var passedPost:Post
    private lateinit var title:TextView
    private lateinit var body:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize view model
        viewModel = (activity as MVVMActivity).viewModel

        //save the post from safeargs to a variable
        passedPost=args.post

        //Initialize the views declared as lateinit
        title = view.findViewById(R.id.single_post_title)
        body = view.findViewById(R.id.single_post_body)
        commentsRecyclerView = view.findViewById(R.id.post_comments_rv)
        commentProgressBar = view.findViewById(R.id.comment_progressbar)

        //Set the text from safeargs to text views
        title.text = passedPost.title
        body.text = passedPost.body
        // send the post id to the view model to request related comments from Api
        viewModel.receiveCommentId(passedPost.id!!)

        setupRecyclerView()
        //Observe the comment list live data in the view model
        viewModel.commentList.observe(viewLifecycleOwner, Observer{ response->
            when(response){
                is Resource.Success->{
                    response.data?.let{comments ->
                        //Toggle progress bar visibility
                        commentProgressBar.visibility= View.INVISIBLE
                        //Pass comment list to adapter
                        commentsRecyclerAdapter.differ.submitList(comments)
                        Log.d("Single Post Fragment", "Posts: You have fetched ${comments.size} comments")

                    }
                }
                is Resource.Error->{
                    response.message?.let { message->
                        //Toggle progress bar visibility
                        commentProgressBar.visibility= View.INVISIBLE
                        Log.d("Single Post Fragment", "Fetch error:$message")
                    }
                }
                is Resource.Loading -> {
                    //Toggle progress bar visibility
                    commentProgressBar.visibility= View.VISIBLE
//                    Toast.makeText(activity,"Loading comments", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    override fun onDestroy(){
        super.onDestroy()
        //Set the comment live data to null when the fragment is destroyed
        viewModel.commentList.value=null
    }

    private fun setupRecyclerView(){
        commentsRecyclerAdapter = CommentsRecyclerAdapter()
        commentsRecyclerView.apply {
            adapter = commentsRecyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}
