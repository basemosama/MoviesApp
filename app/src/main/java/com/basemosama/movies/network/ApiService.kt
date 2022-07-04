package com.basemosama.movies.network

import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.network.utils.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object{
//         const val BASE_URL: String = "https://api.themoviedb.org/3/"
        //const val BASE_URL: String = "http://10.0.2.2:3000/";
        const val BASE_URL: String = "http://192.168.1.104:3000";


    }


    @GET("movie/top_rated")
    suspend fun getTopRatedMovies2(
        @Query("language") language: String,
        @Query("page") page: Long
    ): PagedResponse<Movie>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): NetworkResult<PagedResponse<Movie>>



    @GET("discover/movie")
    suspend fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String,
    ): NetworkResult<PagedResponse<Movie>>


    @GET("movies?_limit=20&_sort=popularity&_order=desc")
    suspend fun getPopularMovies(
        @Query("_page") page: Int
    ): NetworkResult<List<Movie>>

}