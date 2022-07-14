package com.basemosama.movies.data.model.search

import androidx.room.Entity
import java.util.*

@Entity(tableName = "recent_searches", primaryKeys = ["query","movieId"])
data class RecentSearch (
    val query: String ="-1",
    val movieId:Long =-1,
    val updatedAt: Date
    )

