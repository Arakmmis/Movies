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
import yassir.moviesapp.domain.api.Api
import yassir.moviesapp.domain.helpers.MoviesHelper
import yassir.moviesapp.domain.helpers.RetrofitHelper
import yassir.moviesapp.domain.usecases.GetMoviesListUseCase
import java.net.HttpURLConnection

@OptIn(ExperimentalCoroutinesApi::class)
class GetMoviesListUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var api: Api
    private lateinit var mockWebServer: MockWebServer
    private lateinit var getMoviesListUseCase: GetMoviesListUseCase

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = RetrofitHelper.testApiInstance(mockWebServer.url("/").toString())
        repository = MovieRepositoryImpl(api)
        getMoviesListUseCase = GetMoviesListUseCase(repository)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `for a given page, return movies list`() = runTest {
        val movies = MoviesHelper.`get one page of two movies`()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(movies))

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = getMoviesListUseCase.execute(1, QueryHelper.trendingMoviesParams())

        assertThat(actualResponse).hasSize(2)
        assertThat(actualResponse).isEqualTo(movies.results)
    }

    @Test
    fun `for no movies, return empty`() = runTest {
        val moviesPages = MoviesHelper.`get one page of no movies`()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(moviesPages))

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = getMoviesListUseCase.execute(1, QueryHelper.trendingMoviesParams())

        assertThat(actualResponse).hasSize(0)
    }

    @Test
    fun `for server error, throw exception`() = runTest {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)

        mockWebServer.enqueue(expectedResponse)

        try {
            getMoviesListUseCase.execute(1, QueryHelper.trendingMoviesParams())
        } catch (e: Exception) {
            assertThat(e is HttpException)
        }
    }
}