package com.olamachia.simpleblogapp.mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.olamachia.simpleblogapp.R
import com.olamachia.simpleblogapp.mvvm.repository.Repository

class MVVMActivity : AppCompatActivity() {

    lateinit var viewModel: BlogPostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = Repository()
        val viewModelProviderFactory = BlogPostViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(BlogPostViewModel::class.java)

        setContentView(R.layout.activity_mvvmactivity)


    }
}