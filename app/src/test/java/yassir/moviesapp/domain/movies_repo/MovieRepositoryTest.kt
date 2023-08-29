package yassir.moviesapp.domain.movies_repo

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import yassir.moviesapp.domain.Api
import yassir.moviesapp.domain.MovieRepository
import yassir.moviesapp.domain.MovieRepositoryImpl
import yassir.moviesapp.domain.helpers.GsonHelper
import yassir.moviesapp.domain.helpers.MoviesHelper
import yassir.moviesapp.domain.helpers.RetrofitHelper
import yassir.moviesapp.util.QueryHelper
import java.net.HttpURLConnection

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryTest {

    private lateinit var repository: MovieRepository
    private lateinit var api: Api
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = RetrofitHelper.testApiInstance(mockWebServer.url("/").toString())
        repository = MovieRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `on call get movies, returns movies list`() = runTest {
        val movies = MoviesHelper.`get one page of two movies`()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(GsonHelper.moviesPageToJson(movies))

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.getMovies(QueryHelper.trendingMoviesParams())

        assertThat(actualResponse.data?.results).hasSize(2)
        assertThat(actualResponse.data).isEqualTo(movies)
    }
}