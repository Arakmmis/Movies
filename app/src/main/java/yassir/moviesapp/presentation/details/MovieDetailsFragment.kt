package yassir.moviesapp.presentation.details

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import yassir.moviesapp.R
import yassir.moviesapp.data.pojos.MovieDetails
import yassir.moviesapp.databinding.FragMovieDetailsBinding
import yassir.moviesapp.domain.api.ApiConfig
import yassir.moviesapp.presentation.common.LoadingView
import yassir.moviesapp.presentation.details.adapters.ActorsAdapter
import yassir.moviesapp.util.loadImage

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: MovieDetailsViewModel by viewModels()
    private val args: MovieDetailsFragmentArgs by navArgs()

    private lateinit var actorsAdapter: ActorsAdapter

    private var snackbar: Snackbar? = null

    override fun onDestroy() {
        if (snackbar != null && snackbar?.isShown == true)
            snackbar?.dismiss()

        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragMovieDetailsBinding.inflate(inflater, container, false)

        setupViews()
        observeData()

        return binding.root
    }

    private fun setupViews() {
        actorsAdapter = ActorsAdapter()
        binding.rvActors.adapter = actorsAdapter
    }

    private fun observeData() {
        vm.movieDetails.observe(viewLifecycleOwner) {
            updateUi(it)
        }

        vm.progressState.observe(viewLifecycleOwner) { isLoading ->
            with(binding) {
                if (isLoading) {
                    clMovieDetails.visibility = GONE
                    linearLayout.visibility = GONE
                    ivPoster.visibility = GONE
                    loadingView.bind(LoadingView.StateType.LOADING)
                }
            }
        }
    }

    private fun updateUi(details: MovieDetails?) {
        with(binding) {
            if (details != null) {
                if (details.genres != null) {
                    val stringCommaGenre = details.genres.joinToString { it.name.toString() }
                    tvGenreValue.text = stringCommaGenre
                }

                if (!details.overview.isNullOrEmpty()) {
                    tvDescriptionValue.visibility = VISIBLE
                    tvDescriptionTitle.visibility = VISIBLE
                    tvDescriptionValue.text = details.overview
                } else {
                    tvDescriptionValue.visibility = GONE
                    tvDescriptionTitle.visibility = GONE
                }

                actorsAdapter.submitList(details.credits?.cast ?: listOf())

                ivPoster.loadImage(
                    ApiConfig.Constants.BASE_IMAGE_URL_API + details.posterPath
                )
                ivBackdrop.loadImage(
                    ApiConfig.Constants.BASE_IMAGE_URL_w500_API + details.backdropPath
                )

                tvTitleValue.text = details.title
                if (details.title != null && details.title.length > 10)
                    tvTitleValue.isSelected = true

                linearLayout.visibility = VISIBLE
                tvVoteAverage.text = String.format("%.2f", details.voteAverage)

                loadingView.bind(LoadingView.StateType.DONE)

                clMovieDetails.visibility = VISIBLE
            } else {
                clMovieDetails.visibility = GONE
                linearLayout.visibility = GONE
                ivPoster.visibility = GONE
                loadingView.bind(LoadingView.StateType.ERROR)
                showRetrySnackbar()
            }
        }
    }

    private fun showRetrySnackbar() {
        snackbar = Snackbar.make(
            binding.root,
            getString(R.string.err_operational_msg),
            Snackbar.LENGTH_INDEFINITE
        )

        snackbar?.setAction(getText(R.string.retry)) {
            snackbar?.dismiss()
            Handler(Looper.getMainLooper()).postDelayed({
                vm.getMovieDetails(args.movieId)
            }, 250)
        }
        snackbar?.show()
    }
}