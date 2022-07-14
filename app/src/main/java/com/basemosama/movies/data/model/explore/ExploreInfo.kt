package com.basemosama.movies.data.model.explore

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.basemosama.movies.data.model.utils.SortOrder

@Entity(tableName = "explore")
data class ExploreInfo(
    @PrimaryKey
    val exploreId: Long,
    val title: String,
    val sortOrder: SortOrder,
    )