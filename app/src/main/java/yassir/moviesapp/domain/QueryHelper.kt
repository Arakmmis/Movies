package yassir.moviesapp.domain

class QueryHelper {

    companion object {

        private const val KEY_SORT_BY = "sort_by"
        private const val KEY_INCLUDE_VIDEO = "include_video"
        private const val KEY_INCLUDE_ADULT = "include_adult"
        const val KEY_PAGE = "page"
        const val KEY_CREDITS = "credits"

        private const val VALUE_SORT_BY = "popularity.desc"
        private const val VALUE_INCLUDE_VIDEO = "false"
        private const val VALUE_INCLUDE_ADULT = "false"

        fun trendingMoviesParams(): HashMap<String, String> {
            val map = HashMap<String, String>()
            map[KEY_SORT_BY] = VALUE_SORT_BY
            map[KEY_INCLUDE_VIDEO] = VALUE_INCLUDE_VIDEO
            map[KEY_INCLUDE_ADULT] = VALUE_INCLUDE_ADULT

            return map
        }
    }
}