package com.basemosama.movies.database

import androidx.paging.LoadType
import androidx.room.*
import com.basemosama.movies.data.Movie
import com.basemosama.movies.pagination.MovieRemoteKey
import timber.log.Timber

@Dao
interface MovieRemoteKeyDao {

    @Query("SELECT * FROM movie_remote_key_table WHERE movieId = :movieId AND searchQuery = :query LIMIT 1")
    suspend fun getRemoteKey(movieId: Int, query: String): MovieRemoteKey?


    @Query("DELETE FROM movie_remote_key_table WHERE searchQuery = :query")
    suspend fun deleteRemoteKeys(query: String)

    @Transaction
    @Query("DELETE FROM movies WHERE id IN ( SELECT movieId FROM movie_remote_key_table WHERE searchQuery = :query)")
    suspend fun deleteMoviesByRemoteKeys(query: String)

    @Transaction
    suspend fun insertAndDeleteMoviesAndRemoteKeys(
        query: String,
        movies: List<Movie>,
        remoteKeys: List<MovieRemoteKey>,
        loadType: LoadType
    ) {

        if (loadType == LoadType.REFRESH) {
            Timber.d("REMOTE SOURCE DELETING:")

            deleteMoviesByRemoteKeys(query)
            deleteRemoteKeys(query)

        }
        Timber.d("REMOTE SOURCE INSERTING ${movies.size} Movies and ${remoteKeys.size} RemoteKeys :")

        insertMovies(movies)
        insertRemoteKey(remoteKeys)


    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(movieRemoteKey: List<MovieRemoteKey>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)


}