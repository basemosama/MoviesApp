package com.basemosama.movies.data

import androidx.paging.*
import com.basemosama.movies.data.model.SortOrder
import com.basemosama.movies.data.model.explore.ExploreInfo
import com.basemosama.movies.data.model.explore.ExploreMovieCrossRef
import com.basemosama.movies.data.model.search.RecentMovie
import com.basemosama.movies.data.model.search.RecentSearch
import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.database.dao.ExploreDao
import com.basemosama.movies.database.dao.MovieDao
import com.basemosama.movies.database.dao.MovieRemoteKeyDao
import com.basemosama.movies.database.dao.RecentDao
import com.basemosama.movies.network.ApiService
import com.basemosama.movies.network.networkBoundResource
import com.basemosama.movies.network.utils.NetworkResult
import com.basemosama.movies.pagination.MovieRemoteKey
import com.basemosama.movies.pagination.MovieRemoteMediator
import com.basemosama.movies.utils.PreferenceManger
import com.basemosama.movies.utils.Resource
import com.basemosama.movies.utils.getExploreData
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
    private val recentDao: RecentDao,
    private val preferenceManger: PreferenceManger
) {

    companion object {
        private const val PAGE_SIZE = 20
        private const val DEFAULT_SEARCH_QUERY = "DEFAULT_QUERY"
        val config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true
        )
        val searchConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = PAGE_SIZE * 2
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


    @OptIn(ExperimentalPagingApi::class)
    fun getSearchedMovies(search: String = DEFAULT_SEARCH_QUERY) =
        Pager(
            searchConfig,
            remoteMediator = MovieRemoteMediator(
                repository = this,
                SortOrder.POPULAR,
                search.ifEmpty { DEFAULT_SEARCH_QUERY })
        ) {
            getPagedMoviesFromDB(SortOrder.POPULAR, search)
        }.flow

    suspend fun getMoviesByQueryFromApi(query: String, page: Int) =
        apiClient.searchMovies(page, query)

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


    suspend fun shouldFetchExploreItems(): Boolean {
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lastDay = preferenceManger.getExploreLastUpdateDay()
        val shouldFetch = currentDay != lastDay
        return shouldFetch
    }


    suspend fun handleExplore() {
        exploreDao.insertAll(getExploreData())
        val exploreList = exploreDao.getExploreInfo().first()
        val shouldFetch = shouldFetchExploreItems()
        var order = 0L

        if (shouldFetch) {
            for (explore in exploreList) {
                val response = getMoviesBySortOrderFromApi(explore.sortOrder, 1)
                if (response is NetworkResult.Success) {
                    val movies = response.data.results ?: emptyList()
                    val exploreMovieCrossRef = movies.map { movie ->
                        ExploreMovieCrossRef(
                            explore.exploreId,
                            movie.id,
                            order++
                        )
                    }
                    withContext(Dispatchers.IO) {
                        movieDao.insertMovies(movies)
                        exploreDao.insertExploreMovieCrossRef(exploreMovieCrossRef)

                        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                        preferenceManger.saveExploreCurrentUpdateDay(currentDay)
                    }
                }
            }
        }
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


    fun getExploreItems(): Flow<Map<ExploreInfo, List<Movie>>> = exploreDao.getExploreMap()


    fun getRecentSearches() = recentDao.getRecentSearches()

    fun getRecentMovies() = recentDao.getRecentMovies()

    suspend fun clearRecent() = withContext(Dispatchers.IO) {
        recentDao.clearRecent()
    }

    suspend fun insertRecentSearch(query: String) = withContext(Dispatchers.IO) {
        recentDao.insertRecentSearch(RecentSearch(query, Date()))
    }

    suspend fun insertRecentMovie(movie: Movie) = withContext(Dispatchers.IO) {
        recentDao.insertRecentMovie(RecentMovie(movie.id, movie, Date()))
    }
}
