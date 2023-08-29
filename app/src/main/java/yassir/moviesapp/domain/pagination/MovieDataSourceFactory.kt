package yassir.moviesapp.domain.pagination

import androidx.lifecycle.MutableLiveData
import yassir.moviesapp.domain.MovieRepository
import javax.inject.Inject

class MovieDataSourceFactory @Inject constructor(
    private var queryParams: HashMap<String, String>,
    private val repository: MovieRepository
) {

    val movieDataSourceLiveData = MutableLiveData<MovieDataSource>()

    fun build(): MovieDataSource {
        val movieDataSource = MovieDataSource(queryParams, repository)
        movieDataSourceLiveData.postValue(movieDataSource)

        return movieDataSource
    }

    fun getSource() = movieDataSourceLiveData.value

    fun getPaginationState() = movieDataSourceLiveData.value?.getPaginationState()

    fun getLastQueryParams() = queryParams

    fun updateQueryParams(
        queryParams: HashMap<String, String>
    ) {
        this.queryParams = queryParams
        movieDataSourceLiveData.value?.refresh()
    }
}