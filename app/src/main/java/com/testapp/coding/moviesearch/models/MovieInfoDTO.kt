package com.testapp.coding.moviesearch.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieInfoDTO(
    val id: Int,
    val title: String, @SerializedName("release_date") val releaseDate: String,
    @SerializedName("poster_path") val imageUrl: String?,
    val overview: String
) : Parcelable