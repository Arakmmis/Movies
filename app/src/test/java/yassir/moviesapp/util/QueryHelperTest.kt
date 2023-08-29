package yassir.moviesapp.util

import org.junit.Test

class QueryHelperTest {

    private val KEY_SORT_BY = "sort_by"
    private val KEY_INCLUDE_VIDEO = "include_video"
    private val KEY_INCLUDE_ADULT = "include_adult"

    private val VALUE_SORT_BY = "popularity.desc"
    private val VALUE_INCLUDE_VIDEO = "false"
    private val VALUE_INCLUDE_ADULT = "false"

    @Test
    fun `get trending movies required query params`() {
        val requiredParams = HashMap<String, String>()
        requiredParams[KEY_SORT_BY] = VALUE_SORT_BY
        requiredParams[KEY_INCLUDE_VIDEO] = VALUE_INCLUDE_VIDEO
        requiredParams[KEY_INCLUDE_ADULT] = VALUE_INCLUDE_ADULT

        val queryParams = QueryHelper.trendingMoviesParams()

        assert(requiredParams == queryParams) {
            "Query params for getting trending movies don't match required spec"
        }
    }
}