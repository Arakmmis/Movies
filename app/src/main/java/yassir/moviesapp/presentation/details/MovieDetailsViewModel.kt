package yassir.moviesapp.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import yassir.moviesapp.data.pojos.MovieDetails
import yassir.moviesapp.domain.QueryHelper
import yassir.moviesapp.domain.usecases.GetMovieDetailsUseCase
import yassir.moviesapp.util.SingleLiveEvent
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val job = SupervisorJob()
    private val main: CoroutineDispatcher = Dispatchers.Main
    private val default: CoroutineDispatcher = Dispatchers.Default

    private val _movieDetails: MutableLiveData<MovieDetails?> = MutableLiveData()
    val movieDetails: LiveData<MovieDetails?> = _movieDetails

    private val _progressState: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val progressState: LiveData<Boolean> = _progressState

    init {
        val movieId: Int = savedStateHandle["movie_id"] ?: -1

        if (movieId != -1)
            getMovieDetails(movieId)
        else
            _movieDetails.postValue(null)
    }

    fun getMovieDetails(movieId: Int) = GlobalScope.launch(main + job) {
        _progressState.postValue(true)

        try {
            val response = getMovieDetailsUseCase.execute(movieId, QueryHelper.KEY_CREDITS)

            val movieDetails = filterNoPictureActors(response)
            _movieDetails.postValue(movieDetails)
        } catch (e: Exception) {
            Timber.e(e.message)
            _movieDetails.postValue(null)
        }

        _progressState.postValue(false)
    }

    private suspend fun filterNoPictureActors(
        details: MovieDetails
    ): MovieDetails = withContext(default + job) {
        val actorCurrentList = details.credits?.cast ?: arrayListOf()

        if (actorCurrentList.isNotEmpty()) {
            val filteredList = actorCurrentList.filter {
                it.profilePath != null
            }

            details.credits?.cast = filteredList
        }

        details
    }
}