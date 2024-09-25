package com.testapp.coding.moviesearch

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.testapp.coding.moviesearch.models.MovieInfoDTO
import com.testapp.coding.moviesearch.models.MovieSearchDTO
import kotlinx.android.synthetic.main.activity_movie_results.*

class MovieResultsActivity : AppCompatActivity(), MovieSearchAdapter.ItemClickListener {

    private lateinit var mMovieSearchAdapter: MovieSearchAdapter
    private lateinit var mViewModel: MovieResultsViewModel
    private lateinit var mScrollListener: RecyclerViewEndlessScrollListener

    override fun onItemClicked(movieInfoDTO: MovieInfoDTO) {
        MovieDetailsActivity.startActivity(this, movieInfoDTO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_results)

        loadInitialData()
        setupRecyclerView()
    }

    /**
     * Loads initial data which is discover movie results
     */
    private fun loadInitialData() {
        mViewModel = ViewModelProviders.of(this).get(MovieResultsViewModel::class.java)
        // View observes on ViewModels LiveData
        // This is used to observe changes on discover movie results
        mViewModel.getDiscoverMovieResults().observe(this,
            Observer<MovieSearchDTO> { movieSearchDTO: MovieSearchDTO? ->
                updateResults(movieSearchDTO)
                movie_search_progress_bar.visibility = View.GONE
            })
        // Load initial page first
        if (mViewModel.getMovieResults(ConstantsClass.INITIAL_PAGE)) {
            movie_search_progress_bar.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search_movie).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return true
    }

    // Gets called when a new intent gets routed to the activity or when a new search query is entered
    override fun onNewIntent(intent: Intent?) {
        intent?.let {
            // By default the intent would not be set and getIntent would return the previous intent when activity was
            // created so set the intent so that whenever getIntent is called an updated copy is received
            setIntent(intent)
            handleIntent(intent)
        }
    }

    /**
     * When ever the user searches a query make a network call to get the results
     */
    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            mScrollListener.resetScrollState()
            val searchedQuery = intent.getStringExtra(SearchManager.QUERY)
            mViewModel.getMovieSearchResults().observe(this,
                Observer<MovieSearchDTO> { movieSearchDTO: MovieSearchDTO? ->
                    movieSearchDTO?.let {
                        updateResults(movieSearchDTO)
                    }
                    movie_search_progress_bar.visibility = View.GONE
                })
            // Save the query and load initial page for movie search
            searchedQuery?.let {
                mViewModel.mQuery = searchedQuery
                if (mViewModel.getMovieResults(ConstantsClass.INITIAL_PAGE)) {
                    movie_search_progress_bar.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * Updates the movie results
     * @param movieSearchDTO DTO returned by the api
     */
    private fun updateResults(movieSearchDTO: MovieSearchDTO?) {
        if (movieSearchDTO == null) {
            Toast.makeText(this, R.string.error_text, Toast.LENGTH_LONG).show()
            if (mViewModel.shouldDisplayButton()) {
                movie_search_retry_button.visibility = View.VISIBLE
            }
        } else if (mViewModel.shouldUpdateAdapter(movieSearchDTO)) {
            addContentToAdapter(movieSearchDTO.results, mViewModel.shouldClearItems(movieSearchDTO))
        }
    }

    /**
     * Updates the adapter and hides the progress bar
     * @param movieResults results that need to sent to adapter
     * @param clearItems If the items in the adapter have to be cleared or retained
     */
    private fun addContentToAdapter(movieResults: List<MovieInfoDTO>, clearItems: Boolean) {
        mMovieSearchAdapter.updateItems(movieResults, clearItems)
    }

    // Sets up recycler view
    private fun setupRecyclerView() {
        movie_search_retry_button.setOnClickListener {
            movie_search_progress_bar.visibility = View.VISIBLE
            mViewModel.getMovieResults(ConstantsClass.INITIAL_PAGE)
            movie_search_retry_button.visibility = View.GONE
        }

        mMovieSearchAdapter = MovieSearchAdapter(mutableListOf(), this)
        val linearLayoutManager = LinearLayoutManager(this)
        movie_search_recycler_view.layoutManager = linearLayoutManager
        mScrollListener = object : RecyclerViewEndlessScrollListener(linearLayoutManager) {
            override fun loadMore(page: Int, totalItemCount: Int, recyclerView: RecyclerView) {
                mViewModel.getMovieResults(page)
            }
        }
        movie_search_recycler_view.addOnScrollListener(mScrollListener)
        movie_search_recycler_view.adapter = mMovieSearchAdapter
    }
}
