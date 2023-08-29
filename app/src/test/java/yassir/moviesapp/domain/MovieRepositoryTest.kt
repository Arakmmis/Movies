package yassir.moviesapp.domain

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
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
            .setBody(Gson().toJson(movies))

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.getMovies(QueryHelper.trendingMoviesParams())

        assertThat(actualResponse.data?.results).hasSize(2)
        assertThat(actualResponse.data).isEqualTo(movies)
    }

    @Test
    fun `on call get movie details, returns movie`() = runTest {
        val movie = MoviesHelper.getMovieDetails()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(movie))

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.getMovieDetails(1, "query")

        assertThat(actualResponse.data).isEqualTo(movie)
    }
}