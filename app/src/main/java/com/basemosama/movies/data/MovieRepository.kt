package com.basemosama.movies.data

import androidx.paging.*
import com.basemosama.movies.data.model.SortOrder
import com.basemosama.movies.data.model.explore.ExploreInfo
import com.basemosama.movies.data.model.explore.ExploreMovieCrossRef
import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.database.ExploreDao
import com.basemosama.movies.database.MovieDao
import com.basemosama.movies.database.MovieRemoteKeyDao
import com.basemosama.movies.network.ApiService
import com.basemosama.movies.network.networkBoundResource
import com.basemosama.movies.network.utils.NetworkResult
import com.basemosama.movies.pagination.MovieRemoteKey
import com.basemosama.movies.pagination.MovieRemoteMediator
import com.basemosama.movies.utils.PreferenceManger
import com.basemosama.movies.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


class MovieRepository @Inject constructor(
    private val apiClient: ApiService,
    private val movieDao: MovieDao,
    private val movieRemoteKeyDao: MovieRemoteKeyDao,
    private val exploreDao: ExploreDao,
    private val preferenceManger: PreferenceManger
) {

    companion object {
        private const val PAGE_SIZE = 20
        private const val DEFAULT_SEARCH_QUERY = "DEFAULT_QUERY"
        val config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true
        )
    }


    @OptIn(ExperimentalPagingApi::class)
    fun getPagingMovies(sort: SortOrder, search: String = DEFAULT_SEARCH_QUERY) =
        Pager(
            config,
            remoteMediator = MovieRemoteMediator(
                repository = this,
                sort,
                search.ifEmpty { DEFAULT_SEARCH_QUERY })
        ) {
            getPagedMoviesFromDB(sort, search)
        }.flow


    suspend fun getMoviesBySortOrderFromApi(
        sortBy: SortOrder,
        page: Int
    ): NetworkResult<PagedResponse<Movie>> = when (sortBy) {
        SortOrder.POPULAR -> apiClient.getPopularMovies(page)
        SortOrder.TOP_RATED -> apiClient.getTopRatedMovies(page)
        SortOrder.UPCOMING -> apiClient.getUpcomingMovies(page)
        SortOrder.TRENDING -> apiClient.getTrendingMovies(page)
        SortOrder.NOW_PLAYING -> apiClient.getNowPlayingMovies(page)
        SortOrder.BY_TITLE_ASC -> apiClient.getMoviesByTitleASC(page)
        SortOrder.BY_TITLE_DESC -> apiClient.getMoviesByTitleDESC(page)
    }


    private fun getPagedMoviesFromDB(
        sortType: SortOrder = SortOrder.POPULAR,
        search: String
    ): PagingSource<Int, Movie> =
        movieDao.getPagedMovies(sortType, search.ifEmpty { DEFAULT_SEARCH_QUERY })


    fun getMoviesFromDB(): Flow<List<Movie>> = movieDao.getMovies()

    fun getMovieById(id: Long): Flow<Movie> = movieDao.getMovieById(id)

    suspend fun deleteMovies() = withContext(Dispatchers.IO) {
        movieDao.deleteAllMovies()
    }

    private suspend fun insertMoviesToDB(movies: List<Movie>) = withContext(Dispatchers.IO) {
        movieDao.insertMovies(movies)
    }

    suspend fun handleExplore() {
     //exploreDao.insertAll(getExploreData())
        val exploreList = exploreDao.getExploreInfo().first()

        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lastDay = preferenceManger.getExploreLastUpdateDay()
        val shouldFetch = currentDay != lastDay

        if (shouldFetch) {
            for (item in exploreList) {
                val isSuccess = getAndSaveExploreMovies(item)
                if (isSuccess) preferenceManger.saveExploreCurrentUpdateDay(currentDay)
            }
        }
    }


    private suspend fun getAndSaveExploreMovies(explore: ExploreInfo): Boolean {
        val response = getMoviesBySortOrderFromApi(explore.sortOrder, 1)
        if (response is NetworkResult.Success) {
            val movies = response.data.results ?: emptyList()
            var order =0L
            val exploreMovieCrossRef = movies.map { movie ->
                ExploreMovieCrossRef(
                    explore.exploreId,
                    movie.id,
                    order++
                )
            }

            withContext(Dispatchers.IO){
                movieDao.insertMovies(movies)
                exploreDao.insertExploreMovieCrossRef(exploreMovieCrossRef)
            }

            return true
        }
        return false

    }

    fun getMovies(sortType: SortOrder, page: Int): Flow<Resource<List<Movie>>> =
        networkBoundResource(
            query = {
                movieDao.getMovies()
            },
            fetch = { getMoviesBySortOrderFromApi(sortType, page) },
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




    suspend fun insertAndDeleteMoviesAndRemoteKeysToDB(
        query: String,
        sortOrder: SortOrder,
        movies: List<Movie>,
        remoteKeys: List<MovieRemoteKey>,
        loadType: LoadType
    ) = withContext(Dispatchers.IO) {
        movieRemoteKeyDao.insertAndDeleteMoviesAndRemoteKeys(
            query,
            sortOrder,
            movies,
            remoteKeys,
            loadType
        )
    }


    suspend fun getMovieRemoteKey(itemId: Int, query: String, sort: SortOrder): MovieRemoteKey? {
        return movieRemoteKeyDao.getRemoteKey(itemId, query, sort)
    }


    fun getExploreItems():Flow<Map<ExploreInfo,List<Movie>>> = exploreDao.getExploreMap()
}
