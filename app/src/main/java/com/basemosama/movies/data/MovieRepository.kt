package com.basemosama.movies.data

import com.basemosama.movies.network.ApiClient
import com.basemosama.movies.network.ApiService
import com.basemosama.movies.utils.DataState
import kotlinx.coroutines.flow.Flow

object MovieRepository {
    private val apiClient: ApiService = ApiClient.getClient()

    init {

    }

     fun getMovies(): Flow<DataState<PagedResponse<Movie>>> =
        apiClient.getTopRatedMovies("en-US", 1)


}