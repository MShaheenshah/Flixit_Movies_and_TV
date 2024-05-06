package com.programmerpro.cricketlivestreaming.data.models

import kotlinx.serialization.Serializable


@Serializable
data class MovieCategoriesResponseItem(
    val id: String,
    val name: String,
)
