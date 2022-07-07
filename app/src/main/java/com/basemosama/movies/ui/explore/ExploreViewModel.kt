package com.basemosama.movies.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basemosama.movies.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(private val repository :MovieRepository) : ViewModel() {

    init {

        viewModelScope.launch {
            repository.handleExplore()
        }


    }

    val exploreItems = repository.getExploreItems()

}