package yassir.moviesapp.domain

object ApiConfig {

    object Constants {
        const val BASE_URL = "https://api.themoviedb.org/"
        const val API_VERSION = "3"

        const val API_KEY = "-"
    }

    object QueryStrings {
        // Keys
        const val KEY_API_KEY = "api_key"
        const val KEY_LANGUAGE = "language"

        // Values
        const val VALUE_LANGUAGE = "en-US"
    }
}