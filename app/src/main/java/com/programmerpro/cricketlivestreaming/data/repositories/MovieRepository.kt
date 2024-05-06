package com.programmerpro.cricketlivestreaming.data.repositories

import com.programmerpro.cricketlivestreaming.data.entities.MovieCategoryDetails
import com.programmerpro.cricketlivestreaming.data.entities.MovieCategoryList
import com.programmerpro.cricketlivestreaming.data.entities.MovieDetails
import com.programmerpro.cricketlivestreaming.data.entities.MovieList
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getFeaturedMovies(): Flow<MovieList>
    fun getTrendingMovies(): Flow<MovieList>
    fun getTop10Movies(): Flow<MovieList>
    fun getNowPlayingMovies(): Flow<MovieList>
    fun getMovieCategories(): Flow<MovieCategoryList>
    suspend fun getMovieCategoryDetails(categoryId: String): MovieCategoryDetails
    suspend fun getMovieDetails(movieId: String): MovieDetails
    suspend fun searchMovies(query: String): MovieList
    fun getMoviesWithLongThumbnail(): Flow<MovieList>
    fun getMovies(): Flow<MovieList>
    fun getPopularFilmsThisWeek(): Flow<MovieList>
    fun getTVShows(): Flow<MovieList>
    fun getBingeWatchDramas(): Flow<MovieList>
    fun getFavouriteMovies(): Flow<MovieList>
}

