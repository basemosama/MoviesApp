package com.basemosama.movies.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.model.explore.ExploreInfo
import com.basemosama.movies.data.model.explore.ExploreMovieCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface ExploreDao {

    //GET Explore items with movies of each category
    // used the map function so i can control the order of the movies in the each category
    @Query("SELECT explore.*,movies.* FROM explore " +
            "JOIN explore_movies_cross_ref_table ON explore.exploreId= explore_movies_cross_ref_table.exploreId " +
            "JOIN movies ON  explore_movies_cross_ref_table.movieId = movies.movieId " +
            "ORDER BY explore_movies_cross_ref_table.movieOrder ASC")
    fun getExploreMap():Flow<Map<ExploreInfo,List<Movie>>>


    @Query("SELECT * FROM explore ")
    fun getExploreInfo(): Flow<List<ExploreInfo>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ExploreInfo>)

    @Query("DELETE FROM explore")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExploreMovieCrossRef(exploreMovieCrossRef: List<ExploreMovieCrossRef>)

}