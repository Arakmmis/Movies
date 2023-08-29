package yassir.moviesapp.data.pojos

import com.google.gson.annotations.SerializedName

data class GenresList(
    @SerializedName("genres")
    val genres: List<Genre>? = listOf()
) {

    data class Genre(
        @SerializedName("id") val id: Int? = -1,
        @SerializedName("name") val name: String? = ""
    )
}