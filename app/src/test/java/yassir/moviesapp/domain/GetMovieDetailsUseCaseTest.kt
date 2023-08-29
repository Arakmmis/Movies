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
import yassir.moviesapp.domain.usecases.GetMovieDetailsUseCase
import java.io.IOException
import java.net.HttpURLConnection

@OptIn(ExperimentalCoroutinesApi::class)
class GetMovieDetailsUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var api: Api
    private lateinit var mockWebServer: MockWebServer
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = RetrofitHelper.testApiInstance(mockWebServer.url("/").toString())
        repository = MovieRepositoryImpl(api)
        getMovieDetailsUseCase = GetMovieDetailsUseCase(repository)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


    @Test
    fun `on call get movie details, return movie`() = runTest {
        val movie = MoviesHelper.getMovieDetails()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Gson().toJson(movie))

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = getMovieDetailsUseCase.execute(1, "query")

        assertThat(actualResponse).isEqualTo(movie)
    }

    @Test
    fun `for movie id not available, return null movie object`() =
        runTest {
            val expectedResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)

            mockWebServer.enqueue(expectedResponse)

            try {
                getMovieDetailsUseCase.execute(1, "query")
            } catch (e: Exception) {
                assertThat(e is IOException)
            }
        }

    @Test
    fun `for server error, throw exception`() = runTest {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)

        mockWebServer.enqueue(expectedResponse)

        try {
            getMovieDetailsUseCase.execute(1, "query")
        } catch (e: Exception) {
            assertThat(e is HttpException)
        }
    }
}