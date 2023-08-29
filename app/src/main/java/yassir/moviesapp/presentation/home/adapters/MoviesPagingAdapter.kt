package yassir.moviesapp.presentation.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import yassir.moviesapp.R
import yassir.moviesapp.data.pojos.MoviesPage.Movie
import yassir.moviesapp.databinding.ViewMovieBinding
import yassir.moviesapp.databinding.ViewRetryLoadingBinding
import yassir.moviesapp.domain.pagination.PaginationState

class MoviesPagingAdapter(private val listener: OnMovieClicked) :
    PagingDataAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallBack()) {

    private var state: PaginationState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.view_movie -> MovieViewHolder(
                ViewMovieBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            R.layout.view_retry_loading -> RetryLoadingViewHolder(
                ViewRetryLoadingBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.view_movie -> (holder as MovieViewHolder).bind(
                getItem(position),
                listener
            )

            R.layout.view_retry_loading -> (holder as RetryLoadingViewHolder).bind(state, listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount())
            R.layout.view_movie
        else
            R.layout.view_retry_loading
    }

    override fun getItemCount() = super.getItemCount() + if (hasFooter()) 1 else 0

    private fun hasFooter() =
        super.getItemCount() != 0
                && (state == PaginationState.LOADING || state == PaginationState.ERROR)

    @SuppressLint("NotifyDataSetChanged")
    fun updatePaginationState(newState: PaginationState) {
        this.state = newState
        if (newState != PaginationState.LOADING) {
            notifyDataSetChanged()
        }
    }
}

class MovieDiffCallBack : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}