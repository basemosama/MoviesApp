package com.basemosama.movies.ui.movies

import androidx.lifecycle.*
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.utils.DataState
import com.basemosama.movies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private var _movies: MutableStateFlow<DataState<List<Movie>>> =
        MutableStateFlow(DataState.Loading)


    val movies: StateFlow<DataState<List<Movie>>> = _movies

    val movies2: StateFlow<Resource<List<Movie>>> = repository.getMovies2()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Resource.Loading(null))

    val movies3: LiveData<Resource<List<Movie>>> = repository.getMovies2().asLiveData()


    init {
       // getMovies()
    }




private fun getMovies() {


    viewModelScope.launch {
        //_movies.value = DataState.Loading


//            repository.getMoviesFromDB().collect { movies ->
//                if (movies.isNotEmpty()) {
//                    _movies.value = DataState.Success(movies)
//                } else {
//                    _movies.value = DataState.Error("No Movies Found")
//                }
//            }

//
//                    repository.getMovies().collect {
//                         if(it is DataState.Success){
//                             val results = it.data.results
//                             if(results != null){
//                                 _movies.emit(DataState.Success(results))
//                                 repository.insertMoviesToDB(results)
//                             }else{
//                                 _movies.emit(DataState.Error("Couldn't Get Data "))
//                             }
//                         }else if(it is DataState.Loading){
//                             _movies.emit(DataState.Loading)
//                         }else{
//                             _movies.emit(DataState.Error((it as DataState.Error).message))
//                         }
//                     }

    }

}


val currentMovie = MutableLiveData<Movie?>()
fun setMovie(movie: Movie?) {
    currentMovie.value = movie
}

}