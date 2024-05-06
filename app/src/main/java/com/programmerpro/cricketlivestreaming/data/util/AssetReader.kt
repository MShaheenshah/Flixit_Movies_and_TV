package com.programmerpro.cricketlivestreaming.data.util

import android.content.Context
import com.programmerpro.cricketlivestreaming.data.api.ApiService
import com.programmerpro.cricketlivestreaming.data.models.MoviesResponseItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class AssetsReader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiService,
) {
    suspend fun getJsonDataFromAsset(): List<MoviesResponseItem> {
        return try {
            apiService.getMovies()
        } catch (e: IOException) {
            emptyList()
        }
    }

    fun getJsonDataFromAsset(fileName: String, context: Context = this.context): Result<String> {
        return try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            Result.success(jsonString)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}