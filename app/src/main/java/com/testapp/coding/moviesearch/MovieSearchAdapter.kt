package com.testapp.coding.moviesearch

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testapp.coding.moviesearch.models.MovieInfoDTO
import com.testapp.coding.moviesearch.networkcalls.GlideApp
import kotlinx.android.synthetic.main.movie_item_details.view.*

private const val TAG = "MovieSearchAdapter"
// Treating progress dialog type as an integer
private const val PROGRESS_DIALOG_TYPE = 1

class MovieSearchAdapter(
    private val mMovieResults: MutableList<Any>,
    private val context: Context
) :
    RecyclerView.Adapter<MovieSearchAdapter.BaseMovieViewHolder>() {

    companion object {
        const val TYPE_ITEM = 0
        const val TYPE_PROGRESS = 1
    }

    // Interface that needs to implemented by the activities that use this adapter
    interface ItemClickListener {
        fun onItemClicked(movieInfoDTO: MovieInfoDTO)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMovieViewHolder =
        if (viewType == TYPE_ITEM) {
            MovieSearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false))
        } else {
            ProgressBarViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.progress_bar_layout,
                    parent,
                    false
                )
            )
        }

    override fun getItemCount(): Int {
        return mMovieResults.size
    }

    override fun onBindViewHolder(holder: BaseMovieViewHolder, position: Int) {
        holder.bind(mMovieResults[position])
    }

    override fun getItemViewType(position: Int): Int = if (mMovieResults[position] is MovieInfoDTO) {
        TYPE_ITEM
    } else {
        TYPE_PROGRESS
    }

    // Updates the adapter items
    fun updateItems(results: List<MovieInfoDTO>?, clearItem: Boolean) {
        results?.let {
            // When a new search is performed we need to clear the existing data otherwise just add the contents to the
            // existing one
            if (clearItem) {
                mMovieResults.clear()
                mMovieResults.addAll(results)
                mMovieResults.add(PROGRESS_DIALOG_TYPE)
                notifyDataSetChanged()
            } else {
                mMovieResults.remove(PROGRESS_DIALOG_TYPE)
                val size = mMovieResults.size
                mMovieResults.addAll(results)
                mMovieResults.add(1)
                notifyItemRangeChanged(size, mMovieResults.size - size)
            }
        }
    }

    abstract inner class BaseMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(movieInfoDTO: Any?)
    }

    inner class MovieSearchViewHolder(itemView: View) : BaseMovieViewHolder(itemView) {

        override fun bind(movieInfoDTO: Any?) {

            movieInfoDTO?.let {
                if (movieInfoDTO is MovieInfoDTO) {
                    movieInfoDTO.imageUrl?.let { imageUrl ->
                        GlideApp.with(context).load("${ConstantsClass.IMAGES_BASE_URL}$imageUrl")
                            .placeholder(R.mipmap.ic_launcher).into(itemView.movie_item_image_view)
                    }
                    itemView.apply {
                        movie_item_title.text = movieInfoDTO.title
                        movie_item_release_date.text = context.getString(
                            R.string.release_date_value,
                            DateUtil.formatReleaseDate(movieInfoDTO.releaseDate)
                        )

                        setOnClickListener { _ ->
                            try {
                                (context as ItemClickListener).onItemClicked(movieInfoDTO)
                            } catch (e: ClassCastException) {
                                Log.e(TAG, "Activity should implement ItemClickListener")
                            }
                        }
                    }
                }
            }
        }
    }

    inner class ProgressBarViewHolder(itemView: View) : BaseMovieViewHolder(itemView) {
        override fun bind(movieInfoDTO: Any?) {
        }
    }
}