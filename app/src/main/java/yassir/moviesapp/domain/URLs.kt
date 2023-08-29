package yassir.moviesapp.domain

object URLs {

    private const val PATH_DISCOVER = "discover"
    private const val PATH_MOVIE = "movie"

    const val URL_GET_MOVIES = "$PATH_DISCOVER/$PATH_MOVIE"

    const val URL_MOVIE_DETAILS = "$PATH_MOVIE/{movie_id}"
}