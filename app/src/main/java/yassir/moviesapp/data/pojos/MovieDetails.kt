package yassir.moviesapp.data.pojos

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    @SerializedName("id")
    val id: Int? = -1,
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("poster_path")
    val posterPath: String? = "",
    @SerializedName("backdrop_path")
    val backdropPath: String? = "",
    @SerializedName("overview")
    val overview: String? = "",
    @SerializedName("genres")
    val genres: List<Genre>? = listOf(),
    @SerializedName("credits")
    val credits: Credits? = null,
    @SerializedName("popularity")
    val popularity: Double? = 0.0,
    @SerializedName("release_date")
    val releaseDate: String? = "",
    @SerializedName("vote_average")
    val voteAverage: Double? = 0.0,
    @SerializedName("vote_count")
    val voteCount: Int? = 0
) {

    data class Credits(
        @SerializedName("cast") var cast: List<Actor>? = listOf()
    )

    data class Actor(
        @SerializedName("id") val id: Int? = -1,
        @SerializedName("name") val name: String? = "",
        @SerializedName("profile_path") val profilePath: String? = ""
    )

    data class Genre(
        @SerializedName("id") val id: Int? = -1,
        @SerializedName("name") val name: String? = ""
    )
}