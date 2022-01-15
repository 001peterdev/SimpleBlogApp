package com.olamachia.simpleblogapp.mvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import com.olamachia.simpleblogapp.R
import com.olamachia.simpleblogapp.model.Post
import com.olamachia.simpleblogapp.mvvm.ui.BlogPostViewModel
import com.olamachia.simpleblogapp.mvvm.ui.MVVMActivity
import com.olamachia.simpleblogapp.utils.Resource

class AddPostFragment : Fragment() {

    lateinit var sendPostBtn : Button
    lateinit var postBody :EditText
    private lateinit var viewModel: BlogPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MVVMActivity).viewModel

        sendPostBtn = view.findViewById(R.id.send_post_btn)
        postBody = view.findViewById(R.id.send_post_bodyET)


        sendPostBtn.setOnClickListener {
            val post = postBody.text.toString()
            val send = Post(post,1, "A Title",11)
            viewModel.receiveSentPost(send)
        }

        viewModel.sentPost.observe(viewLifecycleOwner, Observer { response->

            when(response){
                is Resource.Success->{
                    response.data?.let{post ->
                        postBody.text = null
                        Log.d("Send Post Fragment", "Posts: $post")
                        Toast.makeText(activity,"Success",Toast.LENGTH_LONG).show()

                    }
                }
                is Resource.Error->{
                    response.message?.let { message->
                        Log.d("Send Post Fragment", "Fetch error:$message")
                        Toast.makeText(activity, message,Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    Toast.makeText(activity,"Loading", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

}