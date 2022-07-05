package com.basemosama.movies.data

enum class SortOrder {
    POPULAR,
    TOP_RATED,
    UPCOMING,
    TRENDING,
    NOW_PLAYING,
    BY_TITLE_ASC,
    BY_TITLE_DESC;


    fun getOrder(): String {
        return when (this) {
            POPULAR -> "popularity.desc"
            TOP_RATED -> "vote_average.desc"
            UPCOMING -> "release_date.asc"
            TRENDING -> "revenue.desc"
            NOW_PLAYING -> "popularity.desc"
            BY_TITLE_ASC -> "original_title.asc"
            BY_TITLE_DESC -> "original_title.desc"
        }
    }


}