package yassir.moviesapp.domain

import yassir.moviesapp.data.helpers.ResWrapper
import yassir.moviesapp.data.pojos.MovieDetails
import yassir.moviesapp.data.pojos.MoviesPage
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val api: Api) : MovieRepository {

    override suspend fun getMovies(query: Map<String, String>): ResWrapper<MoviesPage?> =
        api.getMovies(query).wrap()

    override suspend fun getMovieDetails(movieId: Int, query: String): ResWrapper<MovieDetails> =
        api.getMovieDetails(movieId, query).wrap()
}