package com.programmerpro.cricketlivestreaming.data.api

import com.google.gson.GsonBuilder
import com.programmerpro.cricketlivestreaming.data.models.MoviesResponseItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface ApiService {
    @GET("/movies")
    suspend fun getMovies(): List<MoviesResponseItem>

    companion object{
        operator fun invoke(): ApiService {
            val gson = GsonBuilder()
                .setLenient()
                .registerTypeAdapter(MoviesResponseItem::class.java, MoviesResponseItemInstanceCreator())
                .create()

            return Retrofit.Builder()
                .baseUrl("https://movies-data-k3pl.onrender.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        }
    }
}