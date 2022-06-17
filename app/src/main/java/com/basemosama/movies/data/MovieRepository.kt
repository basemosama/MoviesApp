package com.basemosama.movies.data

import android.content.Context
import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.database.AppDatabase
import com.basemosama.movies.database.MovieDao
import com.basemosama.movies.network.ApiClient
import com.basemosama.movies.network.ApiService
import com.basemosama.movies.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

object MovieRepository {
    private val apiClient: ApiService = ApiClient.getClient()
    private lateinit var movieDao:MovieDao

    operator fun invoke(context: Context): MovieRepository {
        movieDao =  AppDatabase.getDatabase(context).movieDao()
        return this
    }



    fun getMovies(): Flow<DataState<PagedResponse<Movie>>> =
        apiClient.getTopRatedMovies("en-US", 1)

    fun getMoviesFromDB(): Flow<List<Movie>> =
        movieDao.getAllMovies()


    suspend fun insertMoviesToDB(movies:List<Movie>){
        withContext(Dispatchers.IO){
            movieDao.insertMovies(movies)
        }
    }

}