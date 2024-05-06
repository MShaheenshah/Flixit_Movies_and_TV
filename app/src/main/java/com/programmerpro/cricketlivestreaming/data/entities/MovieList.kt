package com.programmerpro.cricketlivestreaming.data.entities

import androidx.compose.runtime.Immutable

@Immutable
data class MovieList(
    val value: List<Movie> = emptyList()
) : List<Movie> by value