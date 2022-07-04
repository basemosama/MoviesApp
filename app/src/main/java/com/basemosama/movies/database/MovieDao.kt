package com.basemosama.movies.database

import androidx.paging.PagingSource
import androidx.room.*
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.SortType
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    fun getPagedMovies(sortType: SortType, searchQuery: String) : PagingSource<Int,Movie> =
        getDefaultPagedMovies(searchQuery.ifEmpty { "DEFAULT_QUERY" })



    @Query("SELECT * FROM movies ORDER BY id DESC")
    fun getMovies(): Flow<List<Movie>>



    @Transaction
    @Query("SELECT * FROM movies" +
            " INNER JOIN movie_remote_key_table on movies.id = movie_remote_key_table.movieId" +
            " WHERE searchQuery = :search" +
            " ORDER BY movie_remote_key_table.id")
    fun getDefaultPagedMovies(search:String): PagingSource<Int,Movie>




    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): Flow<Movie>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Query("DELETE FROM movies WHERE id = :id")
    fun deleteMovieById(id: Int)

}