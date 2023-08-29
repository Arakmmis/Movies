package yassir.moviesapp.domain.usecases

import yassir.moviesapp.data.pojos.MoviesPage
import yassir.moviesapp.domain.MovieRepository
import yassir.moviesapp.domain.QueryHelper
import javax.inject.Inject

class GetMoviesListUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend fun execute(
        page: Int,
        queryParams: HashMap<String, String>
    ): List<MoviesPage.Movie> {
        queryParams[QueryHelper.KEY_PAGE] = page.toString()

        val response = repository.getMovies(queryParams)

        if (response.error != null) {
            throw response.error
        } else
            return response.data?.results ?: listOf()
    }
}