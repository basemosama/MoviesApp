package com.basemosama.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.data.SortType
import com.basemosama.movies.utils.PreferenceManger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository, private val preferenceManger: PreferenceManger
) : ViewModel() {

    private val searchFlow = MutableStateFlow("")
    private val sortFlow = preferenceManger.preferencesFlow

    val movies2 = repository.getPagingMovies().cachedIn(viewModelScope)
//
//    val movies: StateFlow<Resource<List<Movie>>> = sortFlow.combine(searchFlow) { sort, search ->
//        Pair(sort, search)
//    }    //For having timeouts for search query so not overload the server
//        .debounce(600)
//        .distinctUntilChanged()
//        .flatMapLatest { (sort, search) ->
//            repository.getMovies(sort, search)
//        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Resource.Loading())
//

    fun setSearchQuery(query: String) {
        searchFlow.value = query
    }

    fun saveSortType(type: SortType) {
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