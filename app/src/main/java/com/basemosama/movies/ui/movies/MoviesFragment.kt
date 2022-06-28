package com.basemosama.movies.ui.movies

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basemosama.movies.R
import com.basemosama.movies.adapters.MovieAdapter
import com.basemosama.movies.adapters.MovieClickListener
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.SortType
import com.basemosama.movies.databinding.FragmentMoviesBinding
import com.basemosama.movies.utils.Resource
import com.basemosama.movies.utils.onQueryTextChanged
import com.basemosama.movies.utils.repeatOnLifeCycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(), MovieClickListener {
    private lateinit var moviesBinding: FragmentMoviesBinding
    private lateinit var movieAdapter: MovieAdapter
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
        movieAdapter = MovieAdapter(this)

        moviesBinding.moviesRv.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = movieAdapter
        }

        setHasOptionsMenu(true)
    }


    private fun getMovies() {


        repeatOnLifeCycle(viewModel.movies) { state ->

            moviesBinding.animationView.isVisible =
                state is Resource.Loading && state.data.isNullOrEmpty()

            if (state !is Resource.Loading) {
                movieAdapter.submitList(state.data){
                    moviesBinding.moviesRv.scrollToPosition(0)
                }
            }

            if (state is Resource.Error) {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main,menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.onQueryTextChanged(){query ->
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
        val action = MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment2(movie.id)
        findNavController().navigate(action)
    }
}
}


