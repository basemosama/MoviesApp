package com.basemosama.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    val movies3: LiveData<Resource<List<Movie>>> = repository.getMovies2().asLiveData()


    init {
        // getMovies()
    }

    private val _currentMovie = MutableLiveData<Movie?>()
    val currentMovie: LiveData<Movie?>
        get() = _currentMovie

    fun setMovie(movie: Movie?) {
        _currentMovie.value = movie
    }

}