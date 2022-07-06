package com.basemosama.movies.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.data.model.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(private val repository :MovieRepository) : ViewModel() {
    // TODO: Implement the ViewModel

    fun getMovies(sort:SortOrder) = repository.getPagingMovies(sort)
            .cachedIn(viewModelScope)


}