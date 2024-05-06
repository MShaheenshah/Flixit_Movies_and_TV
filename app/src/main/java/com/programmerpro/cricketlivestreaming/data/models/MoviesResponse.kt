package com.programmerpro.cricketlivestreaming.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponseItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("videoUri")
    val videoUri: String,
    @SerializedName("subtitleUri")
    val subtitleUri: String?,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("rankUpDown")
    val rankUpDown: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("fullTitle")
    val fullTitle: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("image_16_9")
    val image_16_9: String,
    @SerializedName("image_2_3")
    val image_2_3: String,
    @SerializedName("runtimeMins")
    val runtimeMins: Int,
    @SerializedName("runtimeStr")
    val runtimeStr: String,
    @SerializedName("plot")
    val plot: String,
    @SerializedName("contentRating")
    val contentRating: String,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("ratingCount")
    val ratingCount: Int,
    @SerializedName("metaCriticRating")
    val metaCriticRating: Int,
    @SerializedName("genres")
    val genres: String,
    @SerializedName("directors")
    val directors: String,
    @SerializedName("stars")
    val stars: String
)
