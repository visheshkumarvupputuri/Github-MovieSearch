package com.testapp.coding.moviesearch.networkcalls

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieRetrofitService {

    const val API_KEY = "876db37c9eb2be92a162285d2d985373"
    private const val baseUrl: String = "https://api.themoviedb.org/3/"
    private var mMovieSearchService: MovieSearch? = null

    fun create(): MovieSearch {
        mMovieSearchService?.let {
            return mMovieSearchService as MovieSearch
        }
        mMovieSearchService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(MovieSearch::class.java)
        return mMovieSearchService as MovieSearch
    }
}