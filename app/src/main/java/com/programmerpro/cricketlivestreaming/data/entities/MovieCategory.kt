package com.programmerpro.cricketlivestreaming.data.entities

import com.programmerpro.cricketlivestreaming.data.models.MovieCategoriesResponseItem

data class MovieCategory(
    val id: String,
    val name: String,
)

fun MovieCategoriesResponseItem.toMovieCategory(): MovieCategory =
    MovieCategory(id, name)
