package com.basemosama.movies.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(val repository: MovieRepository) : ViewModel() {
    private val movieId = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentMovie: StateFlow<Movie?> = movieId
        .flatMapLatest { id ->
            if (id != null) repository.getMovieById(id) else emptyFlow()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    fun setCurrentId(id: Long) {
        movieId.value = id
    }

}