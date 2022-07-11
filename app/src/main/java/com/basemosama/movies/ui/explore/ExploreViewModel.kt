package com.basemosama.movies.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basemosama.movies.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(private val repository :MovieRepository) : ViewModel() {

    init {

        viewModelScope.launch {
            repository.handleExplore()
        }

    }

    val exploreItems = repository.getExploreItems()
    val sliderItemsCount = MutableStateFlow(0)


    fun getTickerFlow(period: Long, initialDelay: Long = 0L) = flow {
        delay(initialDelay)
        var currentPage = 0
        while (true) {
            if (currentPage >= sliderItemsCount.value) {
                currentPage = -1
            }
            Timber.d("Delayed currentPage $currentPage")
            emit(++currentPage)
            delay(period)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),0)


    var currentPage= 0

}