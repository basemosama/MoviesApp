package com.basemosama.movies.pagination

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.basemosama.movies.data.SortOrder


@Entity(tableName = "movie_remote_key_table")
data class MovieRemoteKey(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val searchQuery: String,
    val sortOrder: SortOrder,
    val movieId: Long,
    val nextPage: Int?
) {
    constructor(query: String, sortOrder: SortOrder, movieId: Long, nextPage: Int?) : this(
        null,
        query,
        sortOrder,
        movieId,
        nextPage
    )

}
