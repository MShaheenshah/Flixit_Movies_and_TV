package com.programmerpro.cricketlivestreaming.data.entities

import androidx.compose.runtime.Immutable

@Immutable
data class MovieCategoryList(
    val value: List<MovieCategory> = emptyList()
) : List<MovieCategory> by value
