package com.testapp.coding.moviesearch.networkcalls

import com.testapp.coding.moviesearch.models.MovieSearchDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieSearch {

    @GET("discover/movie")
    fun discoverMovies(
        @Query("api_key") apiKey: String, @Query("language") language: String, @Query("sort_by") sortBy: String, @Query(
            "include_adult"
        ) includeAdult: Boolean, @Query("include_video") includeVideo: Boolean, @Query("page") page: Int
    ): Call<MovieSearchDTO>

    @GET("search/movie")
    fun searchMovies(@Query("api_key") apiKey: String, @Query("query") query: String, @Query("page") page: Int): Call<MovieSearchDTO>
}