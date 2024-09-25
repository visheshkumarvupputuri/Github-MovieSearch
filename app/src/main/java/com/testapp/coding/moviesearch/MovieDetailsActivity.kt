package com.testapp.coding.moviesearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.testapp.coding.moviesearch.models.MovieInfoDTO
import com.testapp.coding.moviesearch.networkcalls.GlideApp
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : AppCompatActivity() {

    companion object {
        private const val MOVIE_INFO = "movieInfo"
        fun startActivity(
            context: Context,
            movieInfoDTO: MovieInfoDTO
        ) {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(MOVIE_INFO, movieInfoDTO)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        populateUI()
    }

    private fun populateUI() {
        val bundle = intent.extras
        val movieInfoDTO = bundle[MOVIE_INFO] as MovieInfoDTO
        movieInfoDTO.imageUrl?.let { imageUrl ->
            GlideApp.with(this).load("${ConstantsClass.IMAGES_BASE_URL}$imageUrl").centerCrop()
                .placeholder(R.mipmap.ic_launcher).into(movie_details_image_view)
        }
        movie_details_title.text = movieInfoDTO.title
        movie_details_release_date_label.text =
                getString(R.string.release_date_value, DateUtil.formatReleaseDate(movieInfoDTO.releaseDate))
        movie_details_overview.text = movieInfoDTO.overview
    }
}