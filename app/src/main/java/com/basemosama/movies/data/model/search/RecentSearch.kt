package com.basemosama.movies.data.model.search

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.basemosama.movies.data.Movie
import java.util.*

@Entity(tableName = "recent_searches")
data class RecentSearch (
    @PrimaryKey
    val query: String,
    val updatedAt: Date
    )

@Entity(tableName = "recent_movies")
data class RecentMovie(
    @PrimaryKey(autoGenerate = false)
    val id:Long? =null,
    @Embedded
    val movie:Movie,
    val updatedAt: Date
)