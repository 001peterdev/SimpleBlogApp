package com.olamachia.simpleblogapp.mvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.simpleblogapp.R
import com.olamachia.simpleblogapp.adapters.SearchRecyclerAdapter
import com.olamachia.simpleblogapp.mvvm.ui.BlogPostViewModel
import com.olamachia.simpleblogapp.mvvm.ui.MVVMActivity
import com.olamachia.simpleblogapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.Observer
import com.olamachia.simpleblogapp.model.Post

class SearchFragment : Fragment() {

    // Create Lateinit variables for components
    private lateinit var viewModel: BlogPostViewModel
    private lateinit var postSearchBar: EditText
    private lateinit var searchRecyclerAdapter: SearchRecyclerAdapter
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchProgressBar: ProgressBar
    private lateinit var noPost:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize viewmodel
        viewModel = (activity as MVVMActivity).viewModel

        //Initialize the views declared as lateinit
        postSearchBar= view .findViewById(R.id.search_posts)
        searchRecyclerView = view.findViewById(R.id.search_posts_rv)
        searchProgressBar = view.findViewById(R.id.search_progressbar)
        noPost = view.findViewById(R.id.no_posts)

        noPost.visibility = View.GONE
        searchProgressBar.visibility= View.INVISIBLE

        // The job that performs the search operation
        var job: Job?= null
        postSearchBar.addTextChangedListener {editable->
            job?.cancel()
            MainScope().launch {
                delay(1500L)
                editable.let {
                    if (editable.toString().isNotEmpty()){
//                        Toast.makeText(activity,editable.toString(), Toast.LENGTH_LONG).show()
                        // Move back to the first element in the recycler view
                        searchRecyclerView.scrollToPosition(0)
                        viewModel.searchPosts(editable.toString())
                        //Close the keyboard when the call has been made
                        postSearchBar.clearFocus()
                    }
                }
            }
        }


        //Observe the searched post live data in the viewmodel
        viewModel.searchedPost.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    response.data?.let{post ->
                        searchProgressBar.visibility= View.INVISIBLE
                        if (post.isEmpty()){noPost.visibility = View.VISIBLE}
                        //Set up recycler view and send search results
                        setupRecyclerView(post)
                        Log.d("All Posts Fragment", "Posts:${post.size}")

                    }
                }
                is Resource.Error->{
                    response.message?.let { message->
                        searchProgressBar.visibility= View.INVISIBLE
                        Log.d("All Posts Fragment", "Fetch error:$message")
                    }
                }
                is Resource.Loading -> {
                    searchProgressBar.visibility= View.VISIBLE
//                    Toast.makeText(activity,"Loading", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    // Function that sets up the recycler view adapter and sends the search results
    private fun setupRecyclerView(posts:List<Post>){
        searchRecyclerAdapter = SearchRecyclerAdapter(posts)
        searchRecyclerView.apply {
            adapter = searchRecyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}