package com.testapp.coding.moviesearch

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

// Threshold after which new data should be loaded
private const val THRESHOLD_TO_LOAD_DATA = 5
private const val STARTING_PAGE = 1

abstract class RecyclerViewEndlessScrollListener(private val linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    private var mCurrentPage = 1
    private var mLoading = true
    private var mPreviousItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = linearLayoutManager.itemCount

        // When loading is true and total items are greater than previous item then it means that the loading has completed
        if (mLoading && (totalItemCount > mPreviousItemCount)) {
            mLoading = false
            mPreviousItemCount = totalItemCount
        }

        // When nothing is loading and if it has reached the threshold value we need to load new data
        if (!mLoading && (linearLayoutManager.findLastVisibleItemPosition() + THRESHOLD_TO_LOAD_DATA) > totalItemCount) {
            loadMore(++mCurrentPage, totalItemCount, recyclerView)
            mLoading = true
        }
    }

    // We need to reset the scroll state every time we begin a new search
    fun resetScrollState() {
        mCurrentPage = STARTING_PAGE
        mPreviousItemCount = 0
        mLoading = true
        linearLayoutManager.scrollToPosition(0)
    }

    // Call api to get more data
    abstract fun loadMore(page: Int, totalItemCount: Int, recyclerView: RecyclerView)
}