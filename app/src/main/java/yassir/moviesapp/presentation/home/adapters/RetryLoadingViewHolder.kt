package yassir.moviesapp.presentation.home.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import yassir.moviesapp.databinding.ViewRetryLoadingBinding
import yassir.moviesapp.domain.pagination.PaginationState

class RetryLoadingViewHolder(
    private val binding: ViewRetryLoadingBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(status: PaginationState?, listener: OnMovieClicked) {
        hideViews()
        setVisibleRightViews(status)
        binding.btnRetry.setOnClickListener { listener.onRetryLoadingMovies() }
    }

    private fun hideViews() {
        binding.tvErrorMessage.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
    }

    private fun setVisibleRightViews(paginationState: PaginationState?) {
        when (paginationState) {
            PaginationState.ERROR -> {
                binding.btnRetry.visibility = View.VISIBLE
                binding.tvErrorMessage.visibility = View.VISIBLE
            }

            else -> {
                // Do Nothing
            }
        }
    }
}