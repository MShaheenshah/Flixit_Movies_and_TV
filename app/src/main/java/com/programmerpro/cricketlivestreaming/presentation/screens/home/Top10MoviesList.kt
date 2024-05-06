package com.programmerpro.cricketlivestreaming.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.programmerpro.cricketlivestreaming.R
import com.programmerpro.cricketlivestreaming.data.entities.Movie
import com.programmerpro.cricketlivestreaming.presentation.common.ItemDirection
import com.programmerpro.cricketlivestreaming.presentation.common.TopMovies

@Composable
fun Top10MoviesList(
    moviesState: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit
) {
    var currentItemIndex by remember { mutableStateOf(0) }
    val isListFocused by remember { mutableStateOf(false) }

    TopMovies(
        itemDirection = ItemDirection.Horizontal,
        movies = moviesState,
        title = if (isListFocused) null
        else stringResource(R.string.top_10_movies_title),
        showItemTitle = !isListFocused,
        onMovieClick = onMovieClick,
        showIndexOverImage = true,
        focusedItemIndex = { focusedIndex -> currentItemIndex = focusedIndex }
    )
}
