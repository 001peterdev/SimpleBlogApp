package com.olamachia.simpleblogapp.mvvm.repository

import com.olamachia.simpleblogapp.api.BlogApi
import com.olamachia.simpleblogapp.api.RetrofitInstance
import com.olamachia.simpleblogapp.model.Comments
import com.olamachia.simpleblogapp.model.Post
import retrofit2.Retrofit

class Repository {
    private var retrofit: Retrofit = RetrofitInstance.instance
    private var blogApi: BlogApi = retrofit.create(BlogApi::class.java)

    suspend fun getBlogPosts()= blogApi.getBlogPosts()
    suspend fun getComments(id:Int) = blogApi.getComments(id)
    suspend fun sendPost(post: Post)= blogApi.sendPost(post)
    suspend fun sendComment(comments: Comments) =blogApi.sendComment(comments)
    suspend fun searchPost(searchQuery:String)=blogApi.searchPosts(searchQuery)
}
