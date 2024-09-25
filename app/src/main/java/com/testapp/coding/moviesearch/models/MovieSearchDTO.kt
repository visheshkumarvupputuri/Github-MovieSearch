package com.testapp.coding.moviesearch.models

import com.google.gson.annotations.SerializedName

data class MovieSearchDTO(
    val page: Int, @SerializedName("total_results") val totalResults: Int, @SerializedName("total_pages") val totalPages: Int,
    val results: List<MovieInfoDTO>
)