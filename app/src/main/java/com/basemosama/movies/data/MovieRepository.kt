package com.basemosama.movies.data

import android.util.Log
import androidx.paging.*
import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.database.AppDatabase
import com.basemosama.movies.database.MovieDao
import com.basemosama.movies.database.MovieRemoteKeyDao
import com.basemosama.movies.network.ApiService
import com.basemosama.movies.network.networkBoundResource
import com.basemosama.movies.network.utils.NetworkResult
import com.basemosama.movies.pagination.MovieRemoteKey
import com.basemosama.movies.pagination.MovieRemoteMediator
import com.basemosama.movies.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class MovieRepository @Inject constructor(
    private val apiClient: ApiService,
    private val movieDao: MovieDao,
    private val movieRemoteKeyDao: MovieRemoteKeyDao,
    private val database: AppDatabase
) {

    companion object {
        private const val PAGE_SIZE =20
        val config = PagingConfig(pageSize = PAGE_SIZE,
        enablePlaceholders = false)
    }

    private val pagingSourceFactory = {
        Timber.d("REMOTE SOURCE getPagingMovies :.")
       getPagedMoviesFromDB(SortType.DEFAULT, "")}


    @OptIn(ExperimentalPagingApi::class)
    fun getPagingMovies() = Pager(
    config = PagingConfig(20),
    remoteMediator = MovieRemoteMediator("",database,apiClient)
    ) {
        Log.d("REMOTE SOURCE", "getting PagingSource")
        movieDao.getDefaultPagedMovies(  "DEFAULT_QUERY" )
    }.flow

    suspend fun insertAndDeleteMoviesAndRemoteKeysToDB(
        query: String,
        movies: List<Movie>,
        remoteKeys: List<MovieRemoteKey>,
        loadType: LoadType
    )= withContext(Dispatchers.IO) {
        movieRemoteKeyDao.insertAndDeleteMoviesAndRemoteKeys(query,movies, remoteKeys, loadType)
    }


    suspend fun getMovieRemoteKey(itemId:Int,query:String):MovieRemoteKey? {
        return movieRemoteKeyDao.getRemoteKey(itemId,query)
    }


    suspend fun getMoviesFromApi(page: Int = 1): NetworkResult<PagedResponse<Movie>> =
        apiClient.getPopularMovies("en-US", page,"popularity.desc")

    private fun getPagedMoviesFromDB(sortType: SortType, search: String): PagingSource<Int, Movie> =
        movieDao.getDefaultPagedMovies( search.ifEmpty { "DEFAULT_QUERY" })


    fun getMoviesFromDB(): Flow<List<Movie>> = movieDao.getMovies()


    suspend fun deleteMovies() = withContext(Dispatchers.IO) {
        movieDao.deleteAllMovies()
    }

    private suspend fun insertMoviesToDB(movies: List<Movie>) = withContext(Dispatchers.IO) {
        movieDao.insertMovies(movies)
    }


    fun getMovies(sortType: SortType, search: String): Flow<Resource<List<Movie>>> =
        networkBoundResource(
            query = {
                Timber.d("Sorting in Repository has changed to $sortType and search to $search")
                movieDao.getSortedMovies(sortType, search)
            },
            fetch = { getMoviesFromApi() },
            saveFetchResult = { response ->
                run {
                    response.results?.let {
                        insertMoviesToDB(it)
                    }
                }
            }, shouldFetch = {
                //should implement your caching strategy
                it.isEmpty()

            }

        )



}
