package com.basemosama.movies.data

import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.database.MovieDao
import com.basemosama.movies.network.ApiService
import com.basemosama.movies.network.networkBoundResource
import com.basemosama.movies.network.utils.NetworkResult
import com.basemosama.movies.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiClient: ApiService,
    private val movieDao: MovieDao
) {


    private suspend fun getMoviesFromApi(): NetworkResult<PagedResponse<Movie>> {
        return apiClient.getTopRatedMovies("en-US", -2)
    }

    fun getMoviesFromDB(): Flow<List<Movie>> =
        movieDao.getAllMovies()


    private suspend fun insertMoviesToDB(movies: List<Movie>) {
        withContext(Dispatchers.IO) {
            movieDao.insertMovies(movies)
        }
    }


    fun getMovies2(): Flow<Resource<List<Movie>>> =
        networkBoundResource(
            query = { movieDao.getAllMovies() },
            fetch = { getMoviesFromApi() },
            saveFetchResult = { response ->
                run {
                    response.results?.let {
                        insertMoviesToDB(it)
                    }
                }
            }, shouldFetch = {
                //should implement your caching strategy
             //   it.isEmpty()
                true
            }

        )
}
