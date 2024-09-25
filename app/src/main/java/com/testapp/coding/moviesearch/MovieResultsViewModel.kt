package com.testapp.coding.moviesearch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.testapp.coding.moviesearch.models.MovieSearchDTO
import com.testapp.coding.moviesearch.networkcalls.NetworkRequests

class MovieResultsViewModel : ViewModel() {

    private lateinit var mMovieResults: MutableLiveData<MovieSearchDTO>
    private val mMovieIntermediateResults = MutableLiveData<MovieSearchDTO>()
    private var mNetworkRequests: NetworkRequests = NetworkRequests()

    private lateinit var mMovieSearchResults: MutableLiveData<MovieSearchDTO>
    private val mMovieIntermediateSearchResults = MutableLiveData<MovieSearchDTO>()

    private var mPreviousPage: Int = 0
    private var mTotalPages: Int = 0
    var mQuery: String? = null

    /**
     * Fetches movie results from the api. As soon as the network call executes the model updates the ViewModel and the
     * ViewModel in-turn updates the View
     * @return LiveData on which the view would observe on
     */
    fun getDiscoverMovieResults(): LiveData<MovieSearchDTO> {
        if (!::mMovieResults.isInitialized) {
            val movieLiveData = mNetworkRequests.fetchInitialMovieSearchResults()
            mMovieResults = Transformations.switchMap(movieLiveData) { movieSearchDTO ->
                mMovieIntermediateResults.value = movieSearchDTO
                mMovieIntermediateResults
            } as MutableLiveData<MovieSearchDTO>
        }
        return mMovieResults
    }

    private fun getInitialMovieResults(page: Int) {
        mNetworkRequests.getDiscoverMovies(page)
    }

    /**
     * Returns LiveData for movie search results that the view can observe on
     */
    fun getMovieSearchResults(): LiveData<MovieSearchDTO> {
        if (!::mMovieSearchResults.isInitialized) {
            val movieLiveData = mNetworkRequests.fetchMovieSearchResults()
            mMovieSearchResults = Transformations.switchMap(movieLiveData) { movieSearchDTO ->
                mMovieIntermediateSearchResults.value = movieSearchDTO
                mMovieIntermediateSearchResults
            } as MutableLiveData<MovieSearchDTO>
        }
        return mMovieSearchResults
    }

    /**
     * Makes a call to get results based on users query
     * @param page page associated with the query
     */
    private fun getMovieQuerySearchResults(page: Int) {
        mQuery?.let {
            mNetworkRequests.getMovieSearchResults(mQuery!!, page)
        }
    }

    /**
     * Returns if the adapter needs to clear off the items
     * @param movieSearchDTO DTO received from API
     * @return true if it needs to clear otherwise false
     */
    fun shouldClearItems(movieSearchDTO: MovieSearchDTO): Boolean {
        return movieSearchDTO.page == ConstantsClass.INITIAL_PAGE
    }

    /**
     * Returns if the adapters data needs to be updated
     * @param movieSearchDTO DRO received from API
     * @return true if it has to update the adapter false otherwise
     */
    fun shouldUpdateAdapter(movieSearchDTO: MovieSearchDTO): Boolean {
        if (movieSearchDTO.page != mPreviousPage) {
            mPreviousPage = movieSearchDTO.page
            mTotalPages = movieSearchDTO.totalPages
            return true
        }
        return false
    }

    /**
     * Calls the respective movie results end point
     * if mQuery is null then discover movie results will be called otherwise search results will be called
     * @param page page associated with query
     */
    fun getMovieResults(page: Int): Boolean {
        // When it is the page to be loaded previous page should be 0
        if (page == ConstantsClass.INITIAL_PAGE) {
            mPreviousPage = 0
            mTotalPages = 0
        }
        return if (mPreviousPage != page) {
            if (mQuery == null) {
                getInitialMovieResults(page)
            } else {
                getMovieQuerySearchResults(page)
            }
            true
        } else {
            false
        }
    }

    // Displays button only when loading first page
    fun shouldDisplayButton(): Boolean {
        return mPreviousPage == 0
    }
}