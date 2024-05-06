package com.programmerpro.cricketlivestreaming.data.repositories

import com.programmerpro.cricketlivestreaming.data.entities.toMovieCategory
import com.programmerpro.cricketlivestreaming.data.util.AssetsReader
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import javax.inject.Inject

class MovieCategoryDataSource @Inject constructor(
    assetsReader: AssetsReader
) {

    private val movieCategoryDataReader = CachedDataReader {
        readMovieCategoryData(assetsReader, StringConstants.Assets.MovieCategories).map {
            it.toMovieCategory()
        }
    }

    suspend fun getMovieCategoryList() = movieCategoryDataReader.read()

}