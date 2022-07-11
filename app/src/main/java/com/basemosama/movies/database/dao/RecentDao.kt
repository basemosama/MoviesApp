package com.basemosama.movies.database.dao

import androidx.room.*
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.model.search.RecentMovie
import com.basemosama.movies.data.model.search.RecentSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentDao {

    @Query("SELECT * FROM recent_searches ORDER BY updatedAt DESC LIMIT 25")
    fun getRecentSearches(): Flow<List<RecentSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearch(recentSearch: RecentSearch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearches(items: List<RecentSearch>)

    @Query("DELETE FROM recent_searches")
    suspend fun deleteRecentSearches()

    //RECENT MOVIES
    @Query("SELECT * FROM recent_movies ORDER BY updatedAt DESC LIMIT 25")
    fun getRecentMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentMovie(recentSearch: RecentMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentMovies(items: List<RecentMovie>)

    @Query("DELETE FROM recent_movies")
    suspend fun deleteRecentMovies()


    @Transaction
    suspend fun clearRecent() {
        deleteRecentMovies()
        deleteRecentSearches()
    }

}