package com.basemosama.movies.ui.movies

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.basemosama.movies.R
import com.basemosama.movies.adapters.MovieClickListener
import com.basemosama.movies.adapters.PagingMovieAdapter
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.SortType
import com.basemosama.movies.databinding.FragmentMoviesBinding
import com.basemosama.movies.utils.onQueryTextChanged
import com.basemosama.movies.utils.repeatOnLifeCycle
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MoviesFragment : Fragment(), MovieClickListener {
    private lateinit var moviesBinding: FragmentMoviesBinding
    private lateinit var pagingMovieAdapter: PagingMovieAdapter
    private val viewModel: MoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        moviesBinding = FragmentMoviesBinding.inflate(inflater, container, false)
        return moviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        getMovies()
    }

    private fun setupUI() {
        pagingMovieAdapter = PagingMovieAdapter(this)
        //pagingMovieAdapter.setHasStableIds(true)

        val gridlayoutManager = GridLayoutManager(context, 3)
        moviesBinding.moviesRv.apply {
            layoutManager = gridlayoutManager
            adapter = pagingMovieAdapter
        }

        setHasOptionsMenu(true)
    }


    private fun getMovies() {

        repeatOnLifeCycle(pagingMovieAdapter.loadStateFlow) { loadStates ->
            val state = loadStates.refresh
            moviesBinding.loadingView.isVisible = state is LoadState.Loading

            if (state is LoadState.Error) {
                val errorMsg = state.error.message
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }

        }

        repeatOnLifeCycle(viewModel.movies2) { data ->
          ///  Timber.d("REMOTE SOURCE SUBMITTING DATA:")

            pagingMovieAdapter.submitData(data)
        }

        repeatOnLifeCycle(pagingMovieAdapter.onPagesUpdatedFlow){
            val updatedMovies :StringBuilder = StringBuilder()
            pagingMovieAdapter.snapshot().forEach{movie->
                updatedMovies.append(movie?.id)
                    .append(", ")
            }

            Timber.d("CURRENT PAGING MOVIES UI: $updatedMovies")
        }


//        //scroll to top after updating the adapter
//        repeatOnLifeCycle(pagingMovieAdapter.loadStateFlow
//            .distinctUntilChangedBy { it.refresh }
//            .filter { it.refresh is LoadState.NotLoading }
//        ) {
//            moviesBinding.moviesRv.scrollToPosition(0)
//        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.onQueryTextChanged() { query ->
            viewModel.setSearchQuery(query)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_asc -> {
                viewModel.saveSortType(SortType.ASC)
                true
            }
            R.id.action_sort_desc -> {
                viewModel.saveSortType(SortType.DESC)
                true
            }
            R.id.action_sort_default -> {
                viewModel.saveSortType(SortType.DEFAULT)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }


    }





    override fun onMovieClickListener(movie: Movie?) {
        Toast.makeText(context, movie?.title, Toast.LENGTH_SHORT).show()
        viewModel.setMovie(movie)

        movie?.id?.let {
            val action = MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment2(it)
            findNavController().navigate(action)
        }
    }
}


