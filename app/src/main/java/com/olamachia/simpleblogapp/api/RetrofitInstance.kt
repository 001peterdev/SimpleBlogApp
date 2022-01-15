package com.olamachia.simpleblogapp.api

import com.olamachia.simpleblogapp.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private var retInstance: Retrofit? = null

    val instance: Retrofit
        get() {
            if (retInstance == null)
                retInstance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retInstance!!
        }
}