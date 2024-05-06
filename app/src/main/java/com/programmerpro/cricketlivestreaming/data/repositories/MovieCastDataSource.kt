package com.programmerpro.cricketlivestreaming.data.repositories

import com.programmerpro.cricketlivestreaming.data.entities.toMovieCast
import com.programmerpro.cricketlivestreaming.data.util.AssetsReader
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import javax.inject.Inject

class MovieCastDataSource @Inject constructor(
    assetsReader: AssetsReader
) {

    private val movieCastDataReader = CachedDataReader {
        readMovieCastData(assetsReader, StringConstants.Assets.MovieCast).map {
            it.toMovieCast()
        }
    }

    suspend fun getMovieCastList() = movieCastDataReader.read()


}