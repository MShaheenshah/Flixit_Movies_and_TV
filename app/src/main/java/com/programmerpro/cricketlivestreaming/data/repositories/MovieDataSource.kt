package com.programmerpro.cricketlivestreaming.data.repositories

import com.programmerpro.cricketlivestreaming.data.entities.Movie
import com.programmerpro.cricketlivestreaming.data.entities.ThumbnailType
import com.programmerpro.cricketlivestreaming.data.entities.toMovie
import com.programmerpro.cricketlivestreaming.data.util.AssetsReader
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import javax.inject.Inject


class MovieDataSource @Inject constructor(
    assetsReader: AssetsReader
) {

    private val top250MovieDataReader = CachedDataReader {
        readMovieData(assetsReader)
    }

    private val mostPopularMovieDataReader = MovieDataReader {
        readMovieData(assetsReader).map {
            it.toMovie()
        }
    }

    private val movieDataReader = MovieDataReader {
        top250MovieDataReader.read().map {
            it.toMovie()
        }
    }

    private var movieWithLongThumbnailDataReader = CachedDataReader {
        top250MovieDataReader.read().map {
            it.toMovie(ThumbnailType.Long)
        }
    }

    private val nowPlayingMovieDataReader = MovieDataReader {
        val originalList = readMovieData(assetsReader)
        if (originalList.size > 40) {
            originalList.subList(40, 50).map {
                it.toMovie()
            }
        } else {
            emptyList()
        }
    }

    suspend fun getMovieList(thumbnailType: ThumbnailType = ThumbnailType.Standard) =
        when (thumbnailType) {
            ThumbnailType.Standard -> movieDataReader.read()
            ThumbnailType.Long -> movieWithLongThumbnailDataReader.read()
        }

    suspend fun getFeaturedMovieList(): List<Movie> {
        val originalList = movieWithLongThumbnailDataReader.read()
        return originalList.filterIndexed { index, _ ->
            listOf(1, 3, 5, 7, 9).contains(index)
        }
    }

    suspend fun getTrendingMovieList(): List<Movie> {
        val originalList = mostPopularMovieDataReader.read()
        return if (originalList.size >= 10) {
            originalList.subList(0, 10)
        } else {
            originalList
        }
    }

    suspend fun getTop10MovieList(): List<Movie> {
        val originalList = movieWithLongThumbnailDataReader.read()
        return if (originalList.size >= 50) {
            originalList.subList(40, 50)
        } else {
            emptyList()
        }
    }

    suspend fun getNowPlayingMovieList() =
        nowPlayingMovieDataReader.read()

    suspend fun getPopularFilmThisWeek() =
        mostPopularMovieDataReader.read().subList(11, 20)

    suspend fun getFavoriteMovieList() =
        movieDataReader.read().subList(0, 28)

}
