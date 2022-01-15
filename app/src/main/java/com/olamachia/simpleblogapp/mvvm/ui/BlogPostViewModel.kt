package com.olamachia.simpleblogapp.mvvm.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olamachia.simpleblogapp.model.Comments
import com.olamachia.simpleblogapp.model.Post
import com.olamachia.simpleblogapp.mvvm.repository.Repository
import com.olamachia.simpleblogapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class BlogPostViewModel(
    private val repository: Repository
    ):ViewModel(){

        // create the Live Data variables
        var sentPost: MutableLiveData<Resource<Post>> = MutableLiveData()
        var sentComment: MutableLiveData<Resource<Comments>> = MutableLiveData()
        var searchedPost: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
        var blogPosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
        var commentList: MutableLiveData<Resource<List<Comments>>> = MutableLiveData()

    init {
        //Make the call to the repository to get the list of posts
        getBlogPosts()
    }

    //make the call to the repository to send a post
    private fun sendPost(post:Post)= viewModelScope.launch (Dispatchers.IO) {
        sentPost.postValue(Resource.Loading())
        val sent = repository.sendPost(post)
        sentPost.postValue(handleSentPost(sent))
    }

    //make the call to the repository to send a post
    private fun sendComment(comments: Comments)= viewModelScope.launch (Dispatchers.IO) {
        sentComment.postValue(Resource.Loading())
        val sent = repository.sendComment(comments)
        sentComment.postValue(handleSentComment(sent))
    }

    private fun handleSentComment(sent: Response<Comments>): Resource<Comments>? {
        if(sent.isSuccessful){
            sent.body()?.let{sentPost->
                return Resource.Success(sentPost)
            }
        }
        return Resource.Error(sent.message())
    }


    //check to see if the post was sent successfully or there was an error
    private fun handleSentPost(sent: Response<Post>): Resource<Post>? {

        if(sent.isSuccessful){
            sent.body()?.let{sentPost->
                return Resource.Success(sentPost)
            }
        }
        return Resource.Error(sent.message())
    }

    //make the call to the repository to get a list of posts
    private fun getBlogPosts()= viewModelScope.launch (Dispatchers.IO){
        blogPosts.postValue(Resource.Loading())
        val posts = repository.getBlogPosts()
        blogPosts.postValue(handleBlogPosts(posts))
        Log.i("View Model", posts.body().toString())
    }

    //make the call to the repository to search posts
    fun searchPosts(searchQuery: String) = viewModelScope.launch {
        searchedPost.postValue(Resource.Loading())
        val response = repository.searchPost(searchQuery)
        searchedPost.postValue(handleBlogPosts(response))
       //Log.i("ZZZZZ", response.body().toString())
    }

    //make the call to the repository to get a list of comments
    private fun getComments(id:Int)= viewModelScope.launch (Dispatchers.IO){
        commentList.postValue(Resource.Loading())
        val comments = repository.getComments(id)
        commentList.postValue(handleComments(comments))
    }

    //check to see if the comments were fetched successfully or there was an error
    private fun handleComments(comments: Response<List<Comments>>): Resource<List<Comments>>? {

        if(comments.isSuccessful){
            comments.body()?.let{comment->
                return Resource.Success(comment)
            }
        }
        return Resource.Error(comments.message())
    }


    //check to see if the posts were fetched successfully or there was an error
    private fun handleBlogPosts(posts: Response<List<Post>>): Resource<List<Post>>? {

        if (posts.isSuccessful){
            posts.body()?.let{post ->
                return Resource.Success(post)
            }
        }
      return Resource.Error(posts.message())
    }

    //The function that receives an id of a post and fetches related comments
    fun receiveCommentId(id:Int){
        getComments(id)
    }

    // the function that receives a post and makes the post call
    fun receiveSentPost(post: Post){
        sendPost(post)
    }
}
