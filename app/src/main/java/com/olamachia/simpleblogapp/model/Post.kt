package com.olamachia.simpleblogapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val body: String?= null,
    val id: Int? = null,
    val title: String? = null,
    val userId: Int? = null
):Parcelable