package com.basemosama.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.adapters.MovieAdapter
import com.basemosama.movies.adapters.MovieClickListener
import com.basemosama.movies.data.Movie
import com.basemosama.movies.databinding.FragmentMoviesBinding
import com.basemosama.movies.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MoviesFragment : Fragment(), MovieClickListener {
    private lateinit var moviesBinding: FragmentMoviesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter
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
        recyclerView = moviesBinding.moviesRv
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        adapter = MovieAdapter(this)
        recyclerView.adapter = adapter
        //  viewModel.setMovies(getMoviesData())
    }


    private fun getMovies() {

        // in ui it's better to  observe livedata as it better in handling lifecycle

        viewModel.movies3.observe(viewLifecycleOwner) { state ->

            moviesBinding.animationView.isVisible =
                state is Resource.Loading && state.data.isNullOrEmpty()

            if(state !is Resource.Loading) {
                adapter.submitList(state.data)
            }

            if (state is Resource.Error) {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }

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

fun <T> Fragment.repeatOnLifeCycle(flow: Flow<T>, collector: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collector)
        }
    }
}
