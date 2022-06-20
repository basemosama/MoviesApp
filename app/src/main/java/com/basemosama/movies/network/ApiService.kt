package com.basemosama.movies.network

import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.network.utils.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object{
         const val BASE_URL: String = "https://api.themoviedb.org/3/"
    }


    @GET("movie/top_rated")
    suspend fun getTopRatedMovies2(
        @Query("language") language: String,
        @Query("page") page: Long
    ): PagedResponse<Movie>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Long
    ): NetworkResult<PagedResponse<Movie>>



}