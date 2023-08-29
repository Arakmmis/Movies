package yassir.moviesapp.domain

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import yassir.moviesapp.data.pojos.MovieDetails
import yassir.moviesapp.data.pojos.MoviesPage

interface Api {

    @GET(URLs.URL_GET_MOVIES)
    suspend fun getMovies(@QueryMap params: Map<String, String>): Response<MoviesPage>

    @GET(URLs.URL_MOVIE_DETAILS)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") value: String
    ): Response<MovieDetails>
}