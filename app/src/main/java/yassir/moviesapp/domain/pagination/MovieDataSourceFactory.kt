package yassir.moviesapp.domain.pagination

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import yassir.moviesapp.domain.usecases.GetMoviesListUseCase
import javax.inject.Inject

class MovieDataSourceFactory @Inject constructor(
    private var queryParams: HashMap<String, String>,
    private val getMoviesListUseCase: GetMoviesListUseCase
) {

    private val movieDataSourceLiveData = MutableLiveData<MovieDataSource>()

    fun build(): MovieDataSource {
        val movieDataSource = MovieDataSource(queryParams, getMoviesListUseCase)
        movieDataSourceLiveData.postValue(movieDataSource)

        return movieDataSource
    }

    fun getSource() = movieDataSourceLiveData.value

    fun getPaginationState() = movieDataSourceLiveData.switchMap {
        it.state
    }

    fun getLastQueryParams() = queryParams

    fun updateQueryParams(
        queryParams: HashMap<String, String>
    ) {
        this.queryParams = queryParams
        movieDataSourceLiveData.value?.refresh()
    }
}