package yassir.moviesapp.presentation.home.adapters

import androidx.recyclerview.widget.RecyclerView
import yassir.moviesapp.data.pojos.MoviesPage.Movie
import yassir.moviesapp.databinding.ViewMovieBinding
import yassir.moviesapp.domain.api.ApiConfig.Constants.BASE_IMAGE_URL_API
import yassir.moviesapp.util.getLocalDateFromCustomFormat
import yassir.moviesapp.util.loadImage

class MovieViewHolder(
    private val binding: ViewMovieBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        movie: Movie?,
        listener: OnMovieClicked
    ) {
        with(binding) {
            if (movie != null) {
                tvTitle.text = movie.title

                var url = BASE_IMAGE_URL_API
                url += if (!movie.backdropPath.isNullOrEmpty())
                    movie.backdropPath
                else
                    movie.posterPath

                if (movie.title != null && movie.title.length > 30)
                    tvTitle.isSelected = true

                tvReleaseYear.text =
                    movie.releaseDate?.getLocalDateFromCustomFormat()?.year.toString()

                ivMovie.loadImage(url)

                root.setOnClickListener { listener.onMovieClicked(movie) }
            }
        }
    }
}