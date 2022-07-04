package com.basemosama.movies.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.basemosama.movies.data.Movie
import com.basemosama.movies.database.AppDatabase
import com.basemosama.movies.network.ApiService
import com.basemosama.movies.network.utils.NetworkResult
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val query: String ="",
    private val database: AppDatabase,
    private val apiClient: ApiService
) : RemoteMediator<Int, Movie>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    private val searchQuery = query.ifEmpty { "DEFAULT_QUERY" }


    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                Timber.d("REMOTE SOURCE last Item RemoteKey  :${remoteKey} ")

                val nextPage = remoteKey?.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                nextPage
            }
        }

        Timber.d("REMOTE SOURCE LOADING Page :${page} ")

        val response = apiClient.getPopularMovies("en-US", page,"popularity.desc")


        if (response is NetworkResult.Success) {
            val movies = response.data.results ?: emptyList()
            val nextPage: Int? =
                if (response.data.page < response.data.totalPages) response.data.page + 1 else null

            val remoteKeys: List<MovieRemoteKey> = movies.map { movie ->
                MovieRemoteKey(searchQuery, movie.id, nextPage)
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    //   Timber.d("REMOTE SOURCE DELETING:")

                    database.movieRemoteKeyDao().deleteMoviesByRemoteKeys(query)
                    database.movieRemoteKeyDao().deleteRemoteKeys(query)

                }
                // Timber.d("REMOTE SOURCE INSERTING ${movies.size} Movies and ${remoteKeys.size} RemoteKeys :")

                database.movieDao().insertMovies(movies)
                database.movieRemoteKeyDao().insertRemoteKey(remoteKeys)


            }


            return MediatorResult.Success(
                endOfPaginationReached = nextPage == null
            )
        } else {
            val error = (response as NetworkResult.Error).errorMessage
            return MediatorResult.Error(Exception(error))
        }


    }



    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): MovieRemoteKey? {

        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                database.withTransaction {
                    database.movieRemoteKeyDao().getRemoteKey(movie.id.toInt(), searchQuery)
                }
            }

    }


}