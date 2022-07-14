package com.basemosama.movies.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.data.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val repository: MovieRepository) : ViewModel() {

    val searchFlow = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val movies = searchFlow
        .debounce(600)
        .distinctUntilChanged()
        .flatMapLatest { search ->
            if (search.isEmpty()) {
                emptyFlow()
            } else {
                repository.getSearchedMovies(search)
                    .cachedIn(viewModelScope)
            }
        }


    val recentSearches = repository.getRecentSearches().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val recentMovies = repository.getRecentMovies().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),
        emptyList()
    )


    fun insertRecentSearch(query: String) {
        viewModelScope.launch {
            repository.insertRecentSearch(query)
        }
    }

    fun insertRecentMovie(movie: Movie) {
        viewModelScope.launch {
            repository.insertRecentMovie(movie)
        }

    }

    fun clearRecent() {
        viewModelScope.launch {
            repository.clearRecent()
        }
    }


    fun setSearchQuery(query: String) {
        searchFlow.value = query
    }
}