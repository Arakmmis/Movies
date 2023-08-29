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
import retrofit2.HttpException
import yassir.moviesapp.data.helpers.ResWrapper
import yassir.moviesapp.domain.api.Api
import yassir.moviesapp.domain.helpers.MoviesHelper
import yassir.moviesapp.domain.helpers.RetrofitHelper
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
    fun `on call get movies, return movies list`() = runTest {
        val movies = MoviesHelper.`get one page of two movies`()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(movies))

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.getMovies(QueryHelper.trendingMoviesParams())

        assertThat(actualResponse.data?.results).hasSize(2)
        assertThat(actualResponse.data).isEqualTo(movies)
        assertThat(actualResponse.httpCode).isEqualTo(HttpURLConnection.HTTP_OK)
        assertThat(actualResponse.error).isNull()
    }

    @Test
    fun `for no movies, return empty with http code 200`() = runTest {
        val moviesPages = MoviesHelper.`get one page of no movies`()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(moviesPages))

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.getMovies(QueryHelper.trendingMoviesParams())

        assertThat(actualResponse.data?.results).hasSize(0)
        assertThat(actualResponse.httpCode).isEqualTo(HttpURLConnection.HTTP_OK)
        assertThat(actualResponse.error).isNull()
    }

    @Test
    fun `on call get movie details, return movie`() = runTest {
        val movie = MoviesHelper.getMovieDetails()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(movie))

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.getMovieDetails(1, "query")

        assertThat(actualResponse.data).isEqualTo(movie)
        assertThat(actualResponse.httpCode).isEqualTo(HttpURLConnection.HTTP_OK)
        assertThat(actualResponse.error).isNull()
    }

    @Test
    fun `for movie id not available, return with http code 404 and null movie object`() =
        runTest {
            val expectedResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)

            mockWebServer.enqueue(expectedResponse)

            val actualResponse = repository.getMovieDetails(1, "query")

            assertThat(actualResponse.httpCode).isEqualTo(HttpURLConnection.HTTP_NOT_FOUND)
            assertThat(actualResponse.data).isNull()
            assertThat(actualResponse.error).isInstanceOf(Exception::class.java)
        }

    @Test
    fun `for movies list with server error`() =
        `for server error, return with http code 5xx and throwable` {
            repository.getMovies(QueryHelper.trendingMoviesParams())
        }

    @Test
    fun `for movie details with server error`() =
        `for server error, return with http code 5xx and throwable` {
            repository.getMovieDetails(1, "query")
        }

    private fun <T> `for server error, return with http code 5xx and throwable`(block: suspend () -> ResWrapper<T>) =
        runTest {
            val expectedResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)

            mockWebServer.enqueue(expectedResponse)

            val actualResponse = block.invoke()

            assertThat(actualResponse.httpCode).isEqualTo(HttpURLConnection.HTTP_INTERNAL_ERROR)
            assertThat(actualResponse.data).isNull()
            assertThat(actualResponse.error).isInstanceOf(HttpException::class.java)
        }
}