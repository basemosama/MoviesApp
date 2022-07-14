package com.basemosama.movies.data.model.details

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey
    var id: Long,
    var name: String?
)