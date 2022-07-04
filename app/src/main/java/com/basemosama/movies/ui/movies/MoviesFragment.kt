package com.basemosama.movies.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basemosama.movies.R
import com.basemosama.movies.adapters.MovieClickListener
import com.basemosama.movies.adapters.PagingMovieAdapter
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.SortType
import com.basemosama.movies.databinding.FragmentMoviesBinding
import com.basemosama.movies.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class MoviesFragment : Fragment(), MovieClickListener {
    private lateinit var moviesBinding: FragmentMoviesBinding
    private lateinit var pagingMovieAdapter: PagingMovieAdapter
    private val viewModel: MoviesViewModel by viewModels()


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
        moviesBinding.moviesRv.apply {
           // setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
            adapter = pagingMovieAdapter

        }

        setHasOptionsMenu(true)
    }





    private fun getMovies() {

//        repeatOnLifeCycle(pagingMovieAdapter.loadStateFlow) { loadStates ->
//            val state = loadStates.refresh
//            moviesBinding.loadingView.isVisible = state is LoadState.Loading
//
//            if (state is LoadState.Error) {
//                val errorMsg = state.error.message
//                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
//            }
//
//        }




        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {


                viewModel.movies.collectLatest {
                    Log.d("REMOTE SOURCE", "SUBMITTING ITEMS")

                    pagingMovieAdapter.submitData(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {

                pagingMovieAdapter.onPagesUpdatedFlow.collectLatest {

                    val movies = StringBuilder("")
                    pagingMovieAdapter.snapshot().items.forEach {
                        movies.append(it.title).append("\n")
                    }
                    Timber.d("REMOTE SOURCE DATA SUBMITTED ITEMS $movies")

                }
            }}

//        lifecycleScope.launchWhenCreated {
//            pagingMovieAdapter.loadStateFlow
//                // Use a state-machine to track LoadStates such that we only transition to
//                // NotLoading from a RemoteMediator load if it was also presented to UI.
//                // Only emit when REFRESH changes, as we only want to react on loads replacing the
//                // list.
//                .distinctUntilChangedBy { it.refresh }
//                // Only react to cases where REFRESH completes i.e., NotLoading.
//                .filter { it.refresh is LoadState.NotLoading }
//                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
//                .collect { moviesBinding.moviesRv.scrollToPosition(0) }
//        }

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


