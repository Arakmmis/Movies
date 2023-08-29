package yassir.moviesapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import yassir.moviesapp.R
import yassir.moviesapp.data.pojos.MoviesPage
import yassir.moviesapp.databinding.FragHomeBinding
import yassir.moviesapp.domain.pagination.PaginationState
import yassir.moviesapp.presentation.MainActivity
import yassir.moviesapp.presentation.common.ErrorView
import yassir.moviesapp.presentation.home.adapters.MoviesPagingAdapter
import yassir.moviesapp.presentation.home.adapters.OnMovieClicked

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMovieClicked, SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragHomeBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private var moviesAdapter: MoviesPagingAdapter? = null

    override fun onDestroy() {
        super.onDestroy()
        moviesAdapter = null
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        vm.getMovies()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeData()
    }

    private fun setupViews() {
        with(binding) {
            (requireActivity() as MainActivity).setSupportActionBar(toolbar)
            (requireActivity() as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

            errorView.bind(ErrorView.StateType.NO_ERROR, null)
            swipe.setOnRefreshListener(this@HomeFragment)

            moviesAdapter = MoviesPagingAdapter(this@HomeFragment)
            rvMovies.layoutManager = gridLayoutManager()
            rvMovies.adapter = moviesAdapter
        }
    }

    private fun observeData() {
        vm.paginationState.observe(viewLifecycleOwner) {
            updateUiPaginationState(it)
            moviesAdapter?.updatePaginationState(it)
        }

        vm.moviesPagedLiveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                moviesAdapter?.submitData(it)
            }
        }
    }

    private fun gridLayoutManager(): RecyclerView.LayoutManager {
        val layoutManager = GridLayoutManager(activity, 2)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (moviesAdapter?.getItemViewType(position)) {
                    R.layout.view_retry_loading -> layoutManager.spanCount
                    else -> 1
                }
            }
        }

        return layoutManager
    }

    private fun updateUiPaginationState(paginationState: PaginationState) {
        with(binding) {
            when (paginationState) {
                PaginationState.LOADING -> {
                    swipe.isRefreshing = true
                    errorView.visibility = GONE
                }

                PaginationState.EMPTY -> {
                    swipe.isRefreshing = false
                    if (moviesAdapter?.snapshot()?.items.isNullOrEmpty()) {
                        errorView.visibility = VISIBLE
                        errorView.bind(ErrorView.StateType.EMPTY, null)
                    }
                }

                PaginationState.ERROR -> {
                    swipe.isRefreshing = false
                    if (moviesAdapter?.snapshot()?.items.isNullOrEmpty()) {
                        errorView.visibility = VISIBLE
                        errorView.bind(ErrorView.StateType.CONNECTION) {
                            onRefresh()
                        }
                    }
                }

                PaginationState.DONE -> {
                    swipe.isRefreshing = false
                    errorView.visibility = GONE
                    errorView.bind(ErrorView.StateType.NO_ERROR, null)
                }
            }
        }
    }

    override fun onRefresh() {
        vm.refresh()
    }

    override fun onMovieClicked(movie: MoviesPage.Movie) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(
                movie.id ?: -1
            )
        )
    }

    override fun onRetryLoadingMovies() {
        vm.refreshFailedRequest()
    }
}