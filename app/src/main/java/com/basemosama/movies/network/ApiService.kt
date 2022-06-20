package com.basemosama.movies.network

import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.utils.DataState
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies2(
        @Query("language") language: String,
        @Query("page") page: Long
    ): PagedResponse<Movie>

    @GET("movie/top_rated")
     fun getTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Long
    ): Flow<DataState<PagedResponse<Movie>>>

}