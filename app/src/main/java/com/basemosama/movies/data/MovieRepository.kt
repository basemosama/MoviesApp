package com.basemosama.movies.data

import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.database.MovieDao
import com.basemosama.movies.network.ApiService
import com.basemosama.movies.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(private val apiClient: ApiService,private val movieDao: MovieDao) {

    fun getMovies(): Flow<DataState<PagedResponse<Movie>>> =
        apiClient.getTopRatedMovies("en-US", 1)

    fun getMoviesFromDB(): Flow<List<Movie>> =
        movieDao.getAllMovies()


    suspend fun insertMoviesToDB(movies: List<Movie>) {
        withContext(Dispatchers.IO) {
            movieDao.insertMovies(movies)
        }
    }

}