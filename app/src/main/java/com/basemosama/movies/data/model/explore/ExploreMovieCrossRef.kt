package com.basemosama.movies.data.model.explore

import androidx.room.Entity


@Entity(tableName = "explore_movies_cross_ref_table", primaryKeys = ["exploreId", "movieId"])
data class ExploreMovieCrossRef(
    val exploreId: Long,
    val movieId: Long,
    val movieOrder: Long = 0
)