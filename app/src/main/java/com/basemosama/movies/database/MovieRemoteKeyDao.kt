package com.basemosama.movies.database

import androidx.paging.LoadType
import androidx.room.*
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.model.SortOrder
import com.basemosama.movies.pagination.MovieRemoteKey

@Dao
interface MovieRemoteKeyDao {

    @Query("SELECT * FROM movie_remote_key_table WHERE movieId = :movieId AND searchQuery = :query AND sortOrder = :sortOrder  LIMIT 1")
    suspend fun getRemoteKey(movieId: Int, query: String, sortOrder: SortOrder): MovieRemoteKey?


    @Query("DELETE FROM movie_remote_key_table WHERE searchQuery = :query AND sortOrder = :sortOrder")
    suspend fun deleteRemoteKeys(query: String, sortOrder: SortOrder)

    @Transaction
    @Query(
        "DELETE FROM movies WHERE id IN ( SELECT movieId FROM movie_remote_key_table " +
                "WHERE searchQuery = :query AND sortOrder = :sortOrder)"
    )
    suspend fun deleteMoviesByRemoteKeys(query: String, sortOrder: SortOrder)

    @Transaction
    suspend fun insertAndDeleteMoviesAndRemoteKeys(
        query: String,
        sortOrder: SortOrder,
        movies: List<Movie>,
        remoteKeys: List<MovieRemoteKey>,
        loadType: LoadType
    ) {

        if (loadType == LoadType.REFRESH) {
            deleteMoviesByRemoteKeys(query, sortOrder)
            deleteRemoteKeys(query, sortOrder)
        }

        insertMovies(movies)
        insertRemoteKey(remoteKeys)
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(movieRemoteKey: List<MovieRemoteKey>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)


}