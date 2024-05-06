package com.programmerpro.cricketlivestreaming.data.repositories

import com.programmerpro.cricketlivestreaming.data.entities.ThumbnailType
import com.programmerpro.cricketlivestreaming.data.entities.toMovie
import com.programmerpro.cricketlivestreaming.data.util.AssetsReader
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import javax.inject.Inject

class TvDataSource @Inject constructor(
    assetsReader: AssetsReader
) {
    private val mostPopularTvShowsReader = CachedDataReader {
        readMovieData(assetsReader)
    }

    suspend fun getTvShowList() = mostPopularTvShowsReader.read().subList(0, 5).map {
        it.toMovie(ThumbnailType.Long)
    }

    suspend fun getBingeWatchDramaList() = mostPopularTvShowsReader.read().subList(6, 15).map {
        it.toMovie()
    }
}