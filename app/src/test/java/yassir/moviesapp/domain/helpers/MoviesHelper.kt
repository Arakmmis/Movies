package yassir.moviesapp.domain.helpers

import yassir.moviesapp.data.pojos.MoviesPage
import kotlin.random.Random
import kotlin.random.nextInt

object MoviesHelper {

    fun `get one page of two movies`(): MoviesPage =
        MoviesPage(
            page = 1,
            totalPages = 1,
            totalResults = 2,
            results = listOf(getMovie(), getMovie())
        )

    private fun getMovie(): MoviesPage.Movie {
        val title = getName()
        val posterPath = getPosterPath()
        val genres = getGenres()

        return MoviesPage.Movie(
            id = Random.nextInt(),
            originalTitle = title,
            posterPath = posterPath,
            backdropPath = posterPath,
            title = title,
            releaseDate = Random.nextInt(2000..2023).toString(),
            genres = genres
        )
    }

    private fun getName(): String {
        val movieNames = arrayOf("Deadpool", "Avengers", "Doctor Strange", "Ant-man", "Naruto")
        val index = Random.nextInt(movieNames.size)
        return movieNames[index]
    }

    private fun getPosterPath(): String {
        val posters = arrayOf(
            "https://shorturl.at/oFIS0",
            "https://shorturl.at/kNPS8",
            "https://shorturl.at/CUY01",
            "https://shorturl.at/lMUZ2"
        )

        val index = Random.nextInt(posters.size)

        return posters[index]
    }

    private fun getGenres(): List<Int> {
        return listOf(
            Random.nextInt(0..10),
            Random.nextInt(0..10),
            Random.nextInt(0..10),
            Random.nextInt(0..10)
        )
    }
}