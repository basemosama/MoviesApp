package com.basemosama.movies.data

import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.database.MovieDao
import com.basemosama.movies.network.ApiService
import com.basemosama.movies.network.networkBoundResource
import com.basemosama.movies.utils.DataState
import com.basemosama.movies.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val apiClient: ApiService,
    private val movieDao: MovieDao
) {

    fun getMovies(): Flow<DataState<PagedResponse<Movie>>> =
        apiClient.getTopRatedMovies("en-US", 1)


    suspend fun getMoviesByCoroutines(): PagedResponse<Movie> {
        return apiClient.getTopRatedMovies2("en-US", 1)
    }


    fun getMoviesFromDB(): Flow<List<Movie>> =
        movieDao.getAllMovies()


    suspend fun insertMoviesToDB(movies: List<Movie>) {
        withContext(Dispatchers.IO) {
            movieDao.insertMovies(movies)
        }
    }


    fun getMovies2() :Flow<Resource<List<Movie>>> = networkBoundResource<List<Movie>, PagedResponse<Movie>>(
            query = { movieDao.getAllMovies() },
            fetch = { getMoviesByCoroutines() },
            saveFetchResult = { response ->
                run {
                    response.results?.let {
                        withContext(Dispatchers.IO) {
                            movieDao.insertMovies(it)
                        }
                    }
                }
            }, shouldFetch = {
            //should implement your caching strategy
        //    it.isEmpty()}
            true}
            )
    }
