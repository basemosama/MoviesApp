package com.basemosama.movies.ui.search

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.R
import com.basemosama.movies.adapters.*
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.model.search.RecentSearch
import com.basemosama.movies.databinding.FragmentSearchBinding
import com.basemosama.movies.utils.repeatOnLifeCycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class SearchFragment : Fragment(), MovieClickListener, RecentSearchAdapter.ItemClickListener {


    private val viewModel: SearchViewModel by activityViewModels()
    private var searchBinding: FragmentSearchBinding? = null
    private var searchRv: RecyclerView? = null

    private lateinit var moviesAdapter: PagingMovieAdapter
    private lateinit var recentMoviesAdapter: MovieAdapter
    private lateinit var recentSearchesAdapter: RecentSearchAdapter
    private var searchView: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return searchBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSearchLayout()
        setUpToolbar()
        setUpRecent()
        getRecent()
        getMovies()
        handleClearingRecent()
    }

    private fun setUpSearchLayout() {
        moviesAdapter = PagingMovieAdapter(this)
        searchRv = searchBinding?.moviesRv
        searchRv?.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = moviesAdapter
        }
    }


    private fun setUpRecent() {

        repeatOnLifeCycle(viewModel.searchFlow) { search ->
            if (search.isEmpty()) {
                searchBinding?.recentMoviesLayout?.isVisible = true
                searchBinding?.searchingMoviesLayout?.isVisible = false
                moviesAdapter.submitData(PagingData.empty())
            } else {
                searchBinding?.recentMoviesLayout?.isVisible = false
                searchBinding?.searchingMoviesLayout?.isVisible = true
            }
        }

        recentMoviesAdapter = MovieAdapter(this, ExploreViewType.POSTER)
        searchBinding?.recentMoviesRv?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recentMoviesAdapter
        }

        recentSearchesAdapter = RecentSearchAdapter(this)
        searchBinding?.recentSearchesRv?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = recentSearchesAdapter
        }


    }

    private fun setUpToolbar() {
        searchView = searchBinding?.moviesSearchView

        searchView?.setQuery(viewModel.searchFlow.value, false)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.insertRecentSearch(query)
                }
                searchView?.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.setSearchQuery(query ?: "")
                return true
            }
        })


    }

    private fun handleClearingRecent() {
        searchBinding?.clearRecentBtn?.setOnClickListener() {
            createAlertDialog()
        }
    }

    private fun getMovies() {

        repeatOnLifeCycle(viewModel.movies) { data ->
            moviesAdapter.submitData(data)
        }


        repeatOnLifeCycle(moviesAdapter.loadStateFlow) { loadStates ->
            val state = loadStates.refresh

            searchBinding?.loadingView?.isVisible = state is LoadState.Loading

            searchRv?.isVisible = state !is LoadState.Loading

            if (state is LoadState.Error) {
                val errorMsg = state.error.message
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        //scroll to top after updating the adapter
        repeatOnLifeCycle(moviesAdapter.loadStateFlow
            .distinctUntilChangedBy { it.refresh }
            .filter { it.refresh is LoadState.NotLoading }
        ) {
            searchBinding?.moviesRv?.scrollToPosition(0)
        }
    }


    private fun getRecent() {
        repeatOnLifeCycle(viewModel.recentMovies) {
            recentMoviesAdapter.submitList(it)
        }
        repeatOnLifeCycle(viewModel.recentSearches) {
            recentSearchesAdapter.submitList(it)
        }
    }


    override fun onMovieClickListener(movie: Movie?) {
        movie?.id?.let {
            viewModel.insertRecentMovie(movie)
            val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun onRecentSearchClickListener(item: RecentSearch?) {
        viewModel.setSearchQuery(item?.query ?: "")
        searchView?.setQuery(item?.query, true)

        item?.query?.isNotEmpty()?.let {
            viewModel.insertRecentSearch(item.query)
        }
    }

    override fun onCopyRecentSearchClickListener(item: RecentSearch?) {
        //viewModel.setSearchQuery(item?.query ?: "")
        searchView?.setQuery(item?.query, false)
    }

    private fun createAlertDialog() {
        AlertDialog.Builder(context, R.style.cancelAlertDialog)
            .apply {
                setTitle("Clear Recent Searches")
                setMessage("Are you sure you want to delete all your recent search?")

                setCancelable(true)
                setPositiveButton("OK") { dialog, which ->
                    viewModel.clearRecent()
                    dialog.cancel()
                }
                setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }

            }.show()

    }


}