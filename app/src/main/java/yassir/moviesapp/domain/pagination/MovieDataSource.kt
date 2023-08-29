package yassir.moviesapp.domain.pagination

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import yassir.moviesapp.data.pojos.MoviesPage
import yassir.moviesapp.domain.MovieRepository
import yassir.moviesapp.domain.QueryHelper.Companion.KEY_PAGE
import yassir.moviesapp.util.SingleLiveEvent
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MovieDataSource @Inject constructor(
    private val queryParams: HashMap<String, String>,
    private val repository: MovieRepository
) : PagingSource<Int, MoviesPage.Movie>() {

    private var state: SingleLiveEvent<PaginationState> = SingleLiveEvent()
    private var job = SupervisorJob()
    private val io: CoroutineContext = Dispatchers.IO
    private val scope = CoroutineScope(getJobErrorHandler() + io + job)

    private var retryQuery: (suspend () -> Any)? = null

    override fun getRefreshKey(state: PagingState<Int, MoviesPage.Movie>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesPage.Movie> {
        retryQuery = { load(params) }

        val page = params.key ?: 1

        val results = executeQuery(page)

        return try {
            LoadResult.Page(
                data = results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (results.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun executeQuery(page: Int): List<MoviesPage.Movie> {
        updateState(PaginationState.LOADING)

        var results: List<MoviesPage.Movie>?

        runBlocking {
            queryParams[KEY_PAGE] = page.toString()

            val response = repository.getMovies(queryParams).data
            retryQuery = null

            if (response?.results != null && response.results.isNotEmpty()) {
                updateState(PaginationState.DONE)
            } else
                updateState(PaginationState.EMPTY)

            results = response?.results
        }

        return results ?: listOf()
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Timber.e(MovieDataSource::class.java.simpleName, e)
        updateState(PaginationState.ERROR)
    }

    private fun updateState(pState: PaginationState) {
        this.state.postValue(pState)
    }

    fun refresh() {
        job.cancelChildren()
        invalidate()
    }

    fun getPaginationState(): LiveData<PaginationState> = state

    fun retryFailedQuery() {
        val prevQuery = retryQuery
        retryQuery = null

        scope.launch { prevQuery?.invoke() }
    }
}