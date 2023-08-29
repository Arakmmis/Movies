package yassir.moviesapp.domain

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import yassir.moviesapp.data.pojos.MoviesPage

interface Api {

    @GET(URLs.URL_GET_MOVIES)
    suspend fun getMovies(@QueryMap params: Map<String, String>): Response<MoviesPage>
}