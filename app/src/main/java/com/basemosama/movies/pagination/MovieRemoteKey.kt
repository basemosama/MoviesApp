package com.basemosama.movies.pagination

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movie_remote_key_table")
data class MovieRemoteKey(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val searchQuery: String,
    val movieId: Long,
    val nextPage: Int?
){
    constructor(query: String, movieId: Long, nextPage: Int?) : this(null, query, movieId, nextPage)

}
