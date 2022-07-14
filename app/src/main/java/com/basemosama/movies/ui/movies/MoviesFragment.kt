package com.basemosama.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.R
import com.basemosama.movies.adapters.MovieClickListener
import com.basemosama.movies.adapters.PagingMovieAdapter
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.model.utils.SortOrder
import com.basemosama.movies.databinding.FragmentMoviesBinding
import com.basemosama.movies.utils.repeatOnLifeCycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class MoviesFragment : Fragment(), MovieClickListener {
    private var moviesBinding: FragmentMoviesBinding? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var pagingMovieAdapter: PagingMovieAdapter
    private val viewModel: MoviesViewModel by activityViewModels()
    private val args: MoviesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        moviesBinding = FragmentMoviesBinding.inflate(inflater, container, false)
        return moviesBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setUpToolbar()
        getMovies()
    }

    private fun setupUI() {
        val sortOrder = args.sortOrder
        //TO BE DECIDED IF I SHOULD SAVE SORT ORDER IN DATA STORE
        viewModel.saveSortType(sortOrder)
        pagingMovieAdapter = PagingMovieAdapter(this)
        recyclerView = moviesBinding?.moviesRv
        recyclerView?.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = pagingMovieAdapter
        }

    }


    private fun getMovies() {

        repeatOnLifeCycle(viewModel.movies) { data ->
            pagingMovieAdapter.submitData(data)
        }


        repeatOnLifeCycle(pagingMovieAdapter.loadStateFlow) { loadStates ->
            val state = loadStates.refresh

            moviesBinding?.loadingView?.isVisible = state is LoadState.Loading

            recyclerView?.isVisible = state !is LoadState.Loading

            if (state is LoadState.Error) {
                val errorMsg = state.error.message
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }

        }

        //scroll to top after updating the adapter
        repeatOnLifeCycle(pagingMovieAdapter.loadStateFlow
            .distinctUntilChangedBy { it.refresh }
            .filter { it.refresh is LoadState.NotLoading }
        ) {
            moviesBinding?.moviesRv?.scrollToPosition(0)
        }


    }

    private fun setUpToolbar() {
        moviesBinding?.toolbar?.apply {
            title = args.title
            if (title == getString(R.string.title_movies)) {
                inflateMenu(R.menu.main)
                moviesBinding?.toolbar?.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_sort_asc -> {
                            viewModel.saveSortType(SortOrder.BY_TITLE_ASC)
                            true
                        }
                        R.id.action_sort_desc -> {
                            viewModel.saveSortType(SortOrder.BY_TITLE_DESC)
                            true
                        }
                        R.id.action_sort_popular -> {
                            viewModel.saveSortType(SortOrder.POPULAR)
                            true
                        }
                        R.id.action_sort_top_rated -> {
                            viewModel.saveSortType(SortOrder.TOP_RATED)
                            true
                        }
                        R.id.action_sort_upcoming -> {
                            viewModel.saveSortType(SortOrder.UPCOMING)
                            true
                        }
                        R.id.action_sort_now_playing -> {
                            viewModel.saveSortType(SortOrder.NOW_PLAYING)
                            true
                        }
                        R.id.action_sort_trending -> {
                            viewModel.saveSortType(SortOrder.TRENDING)
                            true
                        }
                        else -> false
                    }
                }
            } else {
                setNavigationIcon(R.drawable.ic_navigate_back)
                setNavigationOnClickListener {
                    (requireActivity() as AppCompatActivity).onSupportNavigateUp()
                }

            }


        }

    }


    override fun onMovieClickListener(movie: Movie?) {
        // Toast.makeText(context, movie?.title, Toast.LENGTH_SHORT).show()
        movie?.id?.let {
            val action = MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment2(it)
            findNavController().navigate(action)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        moviesBinding = null
        recyclerView = null
    }
}


