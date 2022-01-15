package com.olamachia.simpleblogapp.mvvm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olamachia.simpleblogapp.mvvm.repository.Repository

class BlogPostViewModelProviderFactory(
    val repository: Repository
    ) :ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BlogPostViewModel(repository) as T
    }
}