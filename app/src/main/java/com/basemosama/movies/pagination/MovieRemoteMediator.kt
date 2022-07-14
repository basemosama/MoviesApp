package com.basemosama.movies.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.model.utils.SortOrder
import com.basemosama.movies.network.utils.NetworkResult

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val repository: MovieRepository,
    private val sortOrder: SortOrder,
    private val query: String,


    ) :
    RemoteMediator<Int, Movie>() {

    companion object{
        private const val DEFAULT_SEARCH_QUERY = "DEFAULT_QUERY"
    }

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
            LoadType.REFRESH -> 1
            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {

                val remoteKey = getRemoteKeyForLastItem(state)
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



        val response =if(query ==DEFAULT_SEARCH_QUERY)
            repository.getMoviesBySortOrderFromApi(sortOrder,page)
        else repository.getMoviesByQueryFromApi(query,page)



        if (response is NetworkResult.Success) {
            val movies = response.data.results ?: emptyList()
            val nextPage: Int? =
                if (response.data.page < response.data.totalPages) response.data.page + 1 else null

            val remoteKeys: List<MovieRemoteKey> = movies.map { movie ->
                MovieRemoteKey(query,sortOrder,movie.id,  nextPage)
            }

            repository.insertAndDeleteMoviesAndRemoteKeysToDB(
                query,
                sortOrder,
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
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): MovieRemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                // Get the remote keys of the last item retrieved
                repository.getMovieRemoteKey(movie.id.toInt(), query, sortOrder)
            }

    }


}