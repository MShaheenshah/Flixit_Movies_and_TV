package com.programmerpro.cricketlivestreaming.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import com.programmerpro.cricketlivestreaming.presentation.screens.categories.CategoryMovieListScreen
import com.programmerpro.cricketlivestreaming.presentation.screens.movies.MovieDetailsScreen
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.VideoPlayerScreen

enum class Screens(
    private val args: List<String>? = null,
    val isTabItem: Boolean = false,
    val tabIcon: ImageVector? = null
) {
    Profile(tabIcon = Icons.Default.AccountCircle),
    Home(isTabItem = true, tabIcon = Icons.Default.Home),
    Categories(isTabItem = true, tabIcon = Icons.Default.Category),
    Movies(isTabItem = true, tabIcon = Icons.Default.Movie),
//    Shows(isTabItem = true, tabIcon = Icons.Default.Tv),
    Favourites(isTabItem = true, tabIcon = Icons.Default.Favorite),
    Search(isTabItem = true, tabIcon = Icons.Default.Search),
    CategoryMovieList(listOf(CategoryMovieListScreen.CategoryIdBundleKey)),
    MovieDetails(listOf(MovieDetailsScreen.MovieIdBundleKey)),
    Dashboard,
    VideoPlayer(listOf(VideoPlayerScreen.MovieIdBundleKey));

    operator fun invoke(): String {
        val argList = StringBuilder()
        args?.let { nnArgs ->
            nnArgs.forEach { arg -> argList.append("/{$arg}") }
        }
        return name + argList
    }

    fun withArgs(vararg args: Any): String {
        val destination = StringBuilder()
        args.forEach { arg -> destination.append("/$arg") }
        return name + destination
    }
}
