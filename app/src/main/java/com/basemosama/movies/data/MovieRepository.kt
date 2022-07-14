package com.basemosama.movies.data

import androidx.paging.*
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.model.details.MovieDetails
import com.basemosama.movies.data.model.details.MovieDetailsResponse
import com.basemosama.movies.data.model.explore.ExploreInfo
import com.basemosama.movies.data.model.explore.ExploreMovieCrossRef
import com.basemosama.movies.data.model.search.RecentSearch
import com.basemosama.movies.data.model.utils.SortOrder
import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.database.dao.*
import com.basemosama.movies.network.ApiService
import com.basemosama.movies.network.networkBoundResource
import com.basemosama.movies.network.utils.NetworkResult
import com.basemosama.movies.pagination.MoviePagingSource
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
    private val recentDao: RecentDao,
    private val movieDetailsDao: MovieDetailsDao,
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


    //Explore
    fun getExploreItems(): Flow<Map<ExploreInfo, List<Movie>>> = exploreDao.getExploreMap()

    private suspend fun shouldFetchExploreItems(): Boolean {
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lastDay = preferenceManger.getExploreLastUpdateDay()
        return currentDay != lastDay
    }


    suspend fun handleExplore() {
        //exploreDao.insertAll(getExploreData())
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


    //Paging
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


    fun getSearchedMovies(search: String = DEFAULT_SEARCH_QUERY) =
        Pager(
            searchConfig
        ) {
            MoviePagingSource(this, search)
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
        else -> {
            throw IllegalArgumentException("Unknown sort order")
        }
    }


    private fun getPagedMoviesFromDB(
        sortType: SortOrder = SortOrder.POPULAR,
        search: String
    ): PagingSource<Int, Movie> =
        movieDao.getPagedMovies(sortType, search.ifEmpty { DEFAULT_SEARCH_QUERY })


    suspend fun getMovieRemoteKey(itemId: Int, query: String, sort: SortOrder): MovieRemoteKey? {
        return movieRemoteKeyDao.getRemoteKey(itemId, query, sort)
    }

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


    //Details
    fun getMovieDetailsById(id: Long): Flow<Resource<MovieDetails?>> =
        networkBoundResource<MovieDetails?, MovieDetailsResponse>(
            query = { movieDetailsDao.getMovieDetailsById(id) },
            fetch = { apiClient.getMovieDetails(id) },
            saveFetchResult = { movieDetailsDao.insertMovieDetails(it) },
            shouldFetch = { details ->
                val currentTime = Date().time
                val lastUpdatedAt = details?.movie?.lastUpdatedAt?.time
                lastUpdatedAt == null || currentTime - lastUpdatedAt > 1000 * 60 * 60 * 24
            }
        )


    //Recent Search
    fun getRecentSearches() = recentDao.getRecentSearches()

    fun getRecentMovies() = recentDao.getRecentMovies()

    suspend fun clearRecent() = withContext(Dispatchers.IO) {
        recentDao.clearRecent()
    }

    suspend fun insertRecentSearch(query: String) = withContext(Dispatchers.IO) {
        recentDao.insertRecentSearch(RecentSearch(query, -1, Date()))
    }

    suspend fun insertRecentMovie(movie: Movie) = withContext(Dispatchers.IO) {
        recentDao.insertRecentMovie(movie)
    }
}
