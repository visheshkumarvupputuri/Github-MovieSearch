package com.testapp.coding.moviesearch.networkcalls

import android.arch.lifecycle.MutableLiveData
import com.testapp.coding.moviesearch.ConstantsClass
import com.testapp.coding.moviesearch.models.MovieSearchDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkRequests {

    private lateinit var mMovieNetworkResults: MutableLiveData<MovieSearchDTO>
    private lateinit var mMovieSearchNetworkResults: MutableLiveData<MovieSearchDTO>

    /**
     * Fetches initial movies results that need to be displayed when the user has not searched for any movies
     */
    fun fetchInitialMovieSearchResults(): MutableLiveData<MovieSearchDTO> {
        if (!::mMovieNetworkResults.isInitialized) {
            mMovieNetworkResults = MutableLiveData()
        }
        return mMovieNetworkResults
    }

    /**
     * Fetches movies in ascending order of release dates
     */
    fun getDiscoverMovies(page: Int) {
        MovieRetrofitService.create()
            .discoverMovies(
                MovieRetrofitService.API_KEY,
                ConstantsClass.DISCOVER_MOVIES_LANGUAGE,
                ConstantsClass.DISCOVER_MOVIES_SORT_BY,
                false,
                false,
                page
            ).enqueue(object : Callback<MovieSearchDTO> {
                override fun onFailure(call: Call<MovieSearchDTO>, t: Throwable) {
                    mMovieNetworkResults.value = null
                }

                override fun onResponse(call: Call<MovieSearchDTO>?, response: Response<MovieSearchDTO>?) {
                    if (response != null && response.isSuccessful) {
                        mMovieNetworkResults.value = response.body()
                    } else {
                        mMovieNetworkResults.value = null
                    }
                }
            })
    }

    /**
     * Initializes the LiveData that the ViewModel can observe
     * @return LiveData that the ViewModel can observe on
     */
    fun fetchMovieSearchResults(): MutableLiveData<MovieSearchDTO> {
        if (!::mMovieSearchNetworkResults.isInitialized) {
            mMovieSearchNetworkResults = MutableLiveData()
        }
        return mMovieSearchNetworkResults
    }

    /**
     * Fetches movie results from the api
     * @param query searched query by the user
     */
    fun getMovieSearchResults(query: String, page: Int) {
        MovieRetrofitService.create().searchMovies(MovieRetrofitService.API_KEY, query, page).enqueue(object :
            Callback<MovieSearchDTO> {
            override fun onFailure(call: Call<MovieSearchDTO>, t: Throwable) {
                mMovieSearchNetworkResults.value = null
            }

            override fun onResponse(call: Call<MovieSearchDTO>?, response: Response<MovieSearchDTO>?) {
                if (response != null && response.isSuccessful) {
                    mMovieSearchNetworkResults.value = response.body()
                } else {
                    mMovieSearchNetworkResults.value = null
                }
            }
        })
    }
}