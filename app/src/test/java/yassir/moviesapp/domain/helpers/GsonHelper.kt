package yassir.moviesapp.domain.helpers

import com.google.gson.Gson
import yassir.moviesapp.data.pojos.MoviesPage

object GsonHelper {

    fun moviesPageToJson(moviesPage: MoviesPage): String =
        Gson().toJson(moviesPage)
}