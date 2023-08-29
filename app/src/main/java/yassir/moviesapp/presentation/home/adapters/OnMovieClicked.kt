package yassir.moviesapp.presentation.home.adapters

import yassir.moviesapp.data.pojos.MoviesPage.Movie

interface OnMovieClicked {

    fun onRetryLoadingMovies()

    fun onMovieClicked(movie: Movie)
}