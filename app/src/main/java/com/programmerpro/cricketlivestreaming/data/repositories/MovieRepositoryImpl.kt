package com.programmerpro.cricketlivestreaming.data.repositories

import android.util.Log
import com.programmerpro.cricketlivestreaming.data.entities.MovieCategoryDetails
import com.programmerpro.cricketlivestreaming.data.entities.MovieCategoryList
import com.programmerpro.cricketlivestreaming.data.entities.MovieDetails
import com.programmerpro.cricketlivestreaming.data.entities.MovieList
import com.programmerpro.cricketlivestreaming.data.entities.MovieReviewsAndRatings
import com.programmerpro.cricketlivestreaming.data.entities.ThumbnailType
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import com.programmerpro.cricketlivestreaming.data.util.StringConstants.Movie.Reviewer.DefaultCount
import com.programmerpro.cricketlivestreaming.data.util.StringConstants.Movie.Reviewer.DefaultRating
import com.programmerpro.cricketlivestreaming.data.util.StringConstants.Movie.Reviewer.FreshTomatoes
import com.programmerpro.cricketlivestreaming.data.util.StringConstants.Movie.Reviewer.ReviewerName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val movieDataSource: MovieDataSource,
    private val tvDataSource: TvDataSource,
    private val movieCastDataSource: MovieCastDataSource,
    private val movieCategoryDataSource: MovieCategoryDataSource,
) : MovieRepository {

    override fun getFeaturedMovies() = flow {
        val list = MovieList(movieDataSource.getFeaturedMovieList())
        emit(list)
    }

    override fun getTrendingMovies(): Flow<MovieList> = flow {
        val list = MovieList(movieDataSource.getTrendingMovieList())
        emit(list)
    }

    override fun getTop10Movies(): Flow<MovieList> = flow {
        val list = MovieList(movieDataSource.getTop10MovieList())
        emit(list)
    }

    override fun getNowPlayingMovies(): Flow<MovieList> = flow {
        val list = MovieList(movieDataSource.getNowPlayingMovieList())
        emit(list)
    }

    override fun getMovieCategories() = flow {
        val list = MovieCategoryList(movieCategoryDataSource.getMovieCategoryList())
        emit(list)
    }

    override suspend fun getMovieCategoryDetails(categoryId: String): MovieCategoryDetails {
        val categoryList = movieCategoryDataSource.getMovieCategoryList()
        val category = categoryList.find { categoryId == it.id } ?: categoryList.first()

        val movieList = movieDataSource.getMovieList().shuffled().subList(0, 20)

        return MovieCategoryDetails(
            id = category.id,
            name = category.name,
            movies = MovieList(movieList)
        )
    }

    override suspend fun getMovieDetails(movieId: String): MovieDetails {
        val movieList = movieDataSource.getMovieList()
        val movie = movieList.find { it.id == movieId } ?: movieList.first()
        val similarMovieList = movieList.shuffled().subList(0, 4)
        val castList = movieCastDataSource.getMovieCastList()

        return MovieDetails(
            id = movie.id,
            videoUri = movie.videoUri,
            subtitleUri = movie.subtitleUri,
            posterUri = movie.posterUri,
            name = movie.name,
            description = movie.description,
            pgRating = movie.contentRating,
            releaseDate = movie.releaseData,
            categories = movie.genres,
            duration = movie.runtimeStr,
            director = movie.director,
            castAndCrew = castList,
            status = "Released",
            originalLanguage = "English",
            budget = "$15M",
            revenue = "$40M",
            similarMovies = MovieList(similarMovieList),
            reviewsAndRatings = listOf(
                MovieReviewsAndRatings(
                    reviewerName = FreshTomatoes,
                    reviewerIconUri = StringConstants.Movie.Reviewer.FreshTomatoesImageUrl,
                    reviewCount = "22",
                    reviewRating = "89%"
                ),
                MovieReviewsAndRatings(
                    reviewerName = ReviewerName,
                    reviewerIconUri = StringConstants.Movie.Reviewer.ImageUrl,
                    reviewCount = DefaultCount,
                    reviewRating = DefaultRating
                ),
            )
        )
    }

    override suspend fun searchMovies(query: String): MovieList {
        val filtered = movieDataSource.getMovieList().filter {
            it.name.contains(other = query, ignoreCase = true)
        }
        return MovieList(filtered)
    }

    override fun getMoviesWithLongThumbnail() = flow {
        val list = movieDataSource.getMovieList(ThumbnailType.Long)
        emit(MovieList(list))
    }

    override fun getMovies(): Flow<MovieList> = flow {
        val list = movieDataSource.getMovieList()
        emit(MovieList(list))
    }

    override fun getPopularFilmsThisWeek(): Flow<MovieList> = flow {
        val list = movieDataSource.getPopularFilmThisWeek()
        emit(MovieList(list))
    }

    override fun getTVShows(): Flow<MovieList> = flow {
        val list = tvDataSource.getTvShowList()
        emit(MovieList(list))
    }

    override fun getBingeWatchDramas(): Flow<MovieList> = flow {
        val list = tvDataSource.getBingeWatchDramaList()
        emit(MovieList(list))
    }

    override fun getFavouriteMovies(): Flow<MovieList> = flow {
        val list = movieDataSource.getFavoriteMovieList()
        emit(MovieList(list))
    }

}

