package yassir.moviesapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import yassir.moviesapp.domain.QueryHelper
import yassir.moviesapp.domain.api.ApiConfig
import yassir.moviesapp.domain.pagination.MovieDataSourceFactory
import yassir.moviesapp.domain.pagination.PaginationState
import yassir.moviesapp.domain.usecases.GetMoviesListUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getMoviesListUseCase: GetMoviesListUseCase
) : ViewModel() {

    private var moviesDataSourceFactory = MovieDataSourceFactory(
        QueryHelper.trendingMoviesParams(),
        getMoviesListUseCase
    )

    val moviesPagedLiveData = Pager(
        config = PagingConfig(
            pageSize = ApiConfig.Constants.NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            moviesDataSourceFactory.build()
        }
    )
        .liveData
        .cachedIn(viewModelScope)

    val paginationState: LiveData<PaginationState> = moviesDataSourceFactory.getPaginationState()

    init {
        getMovies()
    }

    fun getMovies() {
        val queryParams = moviesDataSourceFactory.getLastQueryParams()
        moviesDataSourceFactory.updateQueryParams(
            queryParams = queryParams
        )
    }

    fun refreshFailedRequest() =
        moviesDataSourceFactory.getSource()?.retryFailedQuery()

    fun refresh() {
        getMovies()
        moviesDataSourceFactory.getSource()?.refresh()
    }
}