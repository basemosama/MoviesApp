package com.basemosama.movies.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.SortType
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    fun getSortedMovies(sortType: SortType, searchQuery: String) : Flow<List<Movie>> =
        when(sortType){
            SortType.ASC ->  getSortedMoviesASC(searchQuery)
            SortType.DESC -> getSortedMoviesDESC(searchQuery)
            SortType.DEFAULT -> getMovies(searchQuery)
        }

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%'" +
            " OR originalTitle LIKE :search")
    fun getMovies(search:String): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%'" +
            " OR originalTitle LIKE :search" +
            " ORDER BY title ASC")
    fun getSortedMoviesASC(search:String): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%'" +
            " OR originalTitle LIKE :search" +
            " ORDER BY title DESC")
    fun getSortedMoviesDESC(search:String): Flow<List<Movie>>


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