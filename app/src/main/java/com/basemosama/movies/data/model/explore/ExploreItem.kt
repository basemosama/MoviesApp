package com.basemosama.movies.data.model.explore

import com.basemosama.movies.data.model.Movie

data class ExploreItem (
//    @Embedded
    val explore :ExploreInfo,
//    @Relation(
//        parentColumn = "exploreId",
//        entityColumn = "movieId",
//        associateBy = Junction(ExploreMovieCrossRef::class),
//
//    )
    val movies :List<Movie>?



)