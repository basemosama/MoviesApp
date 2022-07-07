package com.basemosama.movies.database

import androidx.paging.PagingSource
import androidx.room.*
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.model.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY movieId DESC")
    fun getMovies(): Flow<List<Movie>>

    @Transaction
    @Query("SELECT * FROM movies" +
            " INNER JOIN movie_remote_key_table on movies.movieId = movie_remote_key_table.movieId" +
            " WHERE searchQuery = :searchQuery AND sortOrder = :sortOrder" +
            " ORDER BY movie_remote_key_table.id")
    fun getPagedMovies(sortOrder: SortOrder, searchQuery: String): PagingSource<Int,Movie>




    @Query("SELECT * FROM movies WHERE movieId = :id")
    fun getMovieById(id: Long): Flow<Movie>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Query("DELETE FROM movies WHERE movieId = :id")
    fun deleteMovieById(id: Int)

}