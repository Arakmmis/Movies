package yassir.moviesapp.domain.usecases

import yassir.moviesapp.data.pojos.MovieDetails
import yassir.moviesapp.domain.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend fun execute(movieId: Int, query: String): MovieDetails {
        val response = repository.getMovieDetails(movieId, query)

        return response.data
            ?: throw response.error
                ?: throw IllegalStateException("Unknown state")
    }
}