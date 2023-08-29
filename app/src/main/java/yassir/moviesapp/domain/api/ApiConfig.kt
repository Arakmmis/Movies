package yassir.moviesapp.domain.api

object ApiConfig {

    object Constants {
        const val BASE_URL = "https://api.themoviedb.org/"
        const val API_VERSION = "3"

        const val BASE_IMAGE_URL_API = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/"
        const val BASE_IMAGE_URL_w500_API = "https://image.tmdb.org/t/p/w500/"

        const val API_KEY = "c9856d0cb57c3f14bf75bdc6c063b8f3"

        const val NETWORK_PAGE_SIZE = 15
    }

    object QueryStrings {
        // Keys
        const val KEY_API_KEY = "api_key"
        const val KEY_LANGUAGE = "language"

        // Values
        const val VALUE_LANGUAGE = "en-US"
    }
}