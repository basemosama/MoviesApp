package com.basemosama.movies.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.MovieRepository
import com.basemosama.movies.network.utils.NetworkResult

class MoviePagingSource(
    private val repository: MovieRepository,
    private val query: String
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {

        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, Movie> {
        // Start refresh at page 1 if undefined.
        val nextPageNumber = params.key ?: 1
        val response = repository.getMoviesByQueryFromApi(query, nextPageNumber)
        return if (response is NetworkResult.Success) {
            val movies = response.data.results ?: emptyList()
            val nextPage: Int? =
                if (response.data.page < response.data.totalPages) response.data.page + 1 else null
            PagingSource.LoadResult.Page(
                data = movies,
                prevKey = null, // Only paging forward.
                nextKey = nextPage
            )
        } else {
            val error = (response as NetworkResult.Error).errorMessage
            PagingSource.LoadResult.Error(Exception(error))
        }

    }

}
