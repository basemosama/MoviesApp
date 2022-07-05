package com.basemosama.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.data.SortOrder
import com.basemosama.movies.utils.PreferenceManger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository, private val preferenceManger: PreferenceManger
) : ViewModel() {

    private val searchFlow = MutableStateFlow("")
    private val sortFlow = preferenceManger.preferencesFlow


    //get movies based on sort oder
    @OptIn(ExperimentalCoroutinesApi::class)
    val movies = sortFlow
        .distinctUntilChanged()
        .flatMapLatest { sort ->
            repository.getPagingMovies(sort)
                .cachedIn(viewModelScope)
        }



    fun setSearchQuery(query: String) {
        searchFlow.value = query
    }

    fun saveSortType(type: SortOrder) {
        viewModelScope.launch {
            preferenceManger.saveSortType(type)
        }
    }

    private val _currentMovie = MutableLiveData<Movie?>()
    val currentMovie: LiveData<Movie?>
        get() = _currentMovie

    fun setMovie(movie: Movie?) {
        _currentMovie.value = movie
    }

}