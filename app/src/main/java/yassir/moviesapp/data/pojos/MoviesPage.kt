package yassir.moviesapp.data.pojos

import com.google.gson.annotations.SerializedName

data class MoviesPage(
    @SerializedName("page")
    val page: Int? = 0,
    @SerializedName("results")
    val results: List<Movie>? = listOf(),
    @SerializedName("total_pages")
    val totalPages: Int? = 0,
    @SerializedName("total_results")
    val totalResults: Int? = 0
) {

    data class Movie(
        @SerializedName("id")
        val id: Int? = -1,
        @SerializedName("original_title")
        val originalTitle: String? = "",
        @SerializedName("poster_path")
        val posterPath: String? = "",
        @SerializedName("backdrop_path")
        val backdropPath: String? = "",
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("release_date")
        val releaseDate: String? = "",
        @SerializedName("genres_id")
        val genres: List<Int>? = null
    )
}