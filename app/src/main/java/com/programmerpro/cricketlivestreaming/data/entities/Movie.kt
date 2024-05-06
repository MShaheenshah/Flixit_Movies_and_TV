package com.programmerpro.cricketlivestreaming.data.entities

import com.programmerpro.cricketlivestreaming.data.models.MoviesResponseItem

data class Movie(
    val id: String,
    val videoUri: String,
    val subtitleUri: String?,
    val posterUri: String,
    val name: String,
    val description: String,
    val director: String,
    val contentRating: String,
    val runtimeStr: String,
    val genres: List<String>,
    val releaseData: String
)

fun MoviesResponseItem.toMovie(thumbnailType: ThumbnailType = ThumbnailType.Standard): Movie {
    val thumbnail = when (thumbnailType) {
        ThumbnailType.Standard -> image_2_3
        ThumbnailType.Long -> image_16_9
    }
    return Movie(
        id,
        videoUri,
        subtitleUri,
        thumbnail,
        title,
        fullTitle,
        directors,
        contentRating,
        runtimeStr,
        genres.split(", ").map { it.trim() },
        releaseDate
    )
}

enum class ThumbnailType {
    Standard,
    Long
}