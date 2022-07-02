package com.basemosama.movies.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.database.AppDatabase
import com.basemosama.movies.network.utils.NetworkResult
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val query: String,
    private val repository: MovieRepository,
    private val db: AppDatabase
) :
    RemoteMediator<Int, Movie>() {
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

        // The network load method takes an optional after=<user.id>
        // parameter. For every page after the first, pass the last user
        // ID to let it continue from where it left off. For REFRESH,
        // pass null to load the first page.


        val page = when (loadType) {
            LoadType.REFRESH -> getRemoteKeyClosestToCurrentPosition(state)?.nextPage ?: 1
            // In this example, you never need to prepend, since REFRESH
            // will always load the first page in the list. Immediately
            // return, reporting end of pagination.
            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                //Timber.d("REMOTE SOURCE APPEND LOAD")

                val remoteKey = getRemoteKeyForLastItem(state)
                //Timber.d("REMOTE SOURCE APPEND LOAD KEY1: LastItem $remoteKey?.nextPage")
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextPage = remoteKey?.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextPage
            }
        }


        Timber.d("REMOTE SOURCE NETWORK LOAD KEY: $page")

        val response = repository.getMoviesFromApi(page)

        if (response is NetworkResult.Success) {
            val movies = response.data.results ?: emptyList()
            val nextPage: Int? =
                if (response.data.page < response.data.totalPages) response.data.page + 1 else null

            //    movies.forEach { it.createdAt = Date() }
            val remoteKeys: List<MovieRemoteKey> = movies.map { movie ->
                MovieRemoteKey(searchQuery, movie.id, response.data.page, nextPage)
            }

//            db.withTransaction {
//                if(loadType == LoadType.REFRESH) {
//                    Timber.d("REMOTE SOURCE NETWORK REFRESH LOAD KEY: $page")
//
//                    db.movieDao().deleteAllMovies()
//                    db.movieRemoteKeyDao().deleteMoviesByRemoteKeys(searchQuery)
//                }
//
//                db.movieDao().insertMovies(movies)
//                db.movieRemoteKeyDao().insertRemoteKey(remoteKeys)
//
//            }


            repository.insertAndDeleteMoviesAndRemoteKeysToDB(
                searchQuery,
                movies,
                remoteKeys,
                loadType
            )


            return MediatorResult.Success(
                endOfPaginationReached = nextPage == null
            )
        } else {
            val error = (response as NetworkResult.Error).errorMessage
            return MediatorResult.Error(Exception(error))
        }


        // Suspending network load via Retrofit. This doesn't need to be
        // wrapped in a withContext(Dispatcher.IO) { ... } block since
        // Retrofit's Coroutine CallAdapter dispatches on a worker

    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>):
            MovieRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                repository.getMovieRemoteKey(movieId.toInt(), searchQuery)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): MovieRemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                // Get the remote keys of the last item retrieved
                repository.getMovieRemoteKey(movie.id.toInt(), searchQuery)
            }

    }


}