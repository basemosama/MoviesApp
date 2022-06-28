package com.basemosama.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.data.SortType
import com.basemosama.movies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    // val movies3: LiveData<Resource<List<Movie>>> = repository.getMovies("").asLiveData()
    val sortFlow = MutableStateFlow(SortType.DEFAULT)

    private val searchFlow = MutableStateFlow("")



    val movies: StateFlow<Resource<List<Movie>>> = sortFlow.combine(searchFlow) { sort, search ->
        Pair(sort, search)
    }    //For having timeouts for search query so not overload the server
        .debounce(600)
        .distinctUntilChanged()
        .flatMapLatest { (sort, search) ->
            repository.getMovies(sort, search)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Resource.Loading())


//    init {
//        searchFlow.mapLatest { search ->
//            Timber.d("Sorting $search")
//            movies= repository.getMovies(SortType.DEFAULT, search).stateIn(
//                viewModelScope,
//                SharingStarted.WhileSubscribed(), Resource.Loading()
//            )
//
//        }.launchIn(viewModelScope)
//    }


//
//    val movies3: StateFlow<Resource<List<Movie>>> = combine(sortFlow, searchFlow) { sort, search ->
//        repository.getMovies(sort, search)
//            .stateIn(
//                viewModelScope,
//                SharingStarted.WhileSubscribed(5000),
//                Resource.Loading(null)
//            )
//
//
//    }


    fun setSearchQuery(query: String) {
        searchFlow.value = query
    }

    private val _currentMovie = MutableLiveData<Movie?>()
    val currentMovie: LiveData<Movie?>
        get() = _currentMovie

    fun setMovie(movie: Movie?) {
        _currentMovie.value = movie
    }

}