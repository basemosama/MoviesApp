package com.basemosama.movies.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.data.model.details.MovieDetails
import com.basemosama.movies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(val repository: MovieRepository) : ViewModel() {
    private val movieId = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentMovie: StateFlow<Resource<MovieDetails?>> = movieId
        .flatMapLatest { id ->
            if (id != null) repository.getMovieDetailsById(id) else emptyFlow()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Resource.Loading())


    fun setCurrentId(id: Long) {
        movieId.value = id
    }

}