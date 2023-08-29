package yassir.moviesapp.domain

import yassir.moviesapp.data.helpers.ResWrapper
import yassir.moviesapp.data.pojos.MovieDetails
import yassir.moviesapp.data.pojos.MoviesPage

interface MovieRepository {

    suspend fun getMovies(query: Map<String, String>): ResWrapper<MoviesPage?>

    suspend fun getMovieDetails(movieId: Int, query: String): ResWrapper<MovieDetails>
}