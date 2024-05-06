package com.programmerpro.cricketlivestreaming.presentation.screens.favourites

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Handler
import android.view.OrientationEventListener
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programmerpro.cricketlivestreaming.data.entities.MovieList
import com.programmerpro.cricketlivestreaming.must.ads.Admob_interstitial
import com.programmerpro.cricketlivestreaming.presentation.common.rememberChildPadding

@Composable
fun FavouritesScreen(
    onMovieClick: (movieId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    isTopBarVisible: Boolean,
    favouriteScreenViewModel: FavouriteScreenViewModel = hiltViewModel()
) {
    val uiState by favouriteScreenViewModel.uiState.collectAsStateWithLifecycle()
    when (val s = uiState) {
        is FavouriteScreenUiState.Loading -> {
            Loading()
        }
        is FavouriteScreenUiState.Ready -> {
            Catalog(
                favouriteMovieList = s.favouriteMovieList,
                filterList = FavouriteScreenViewModel.filterList,
                selectedFilterList = s.selectedFilterList,
                onMovieClick = onMovieClick,
                onScroll = onScroll,
                onSelectedFilterListUpdated = favouriteScreenViewModel::updateSelectedFilterList,
                isTopBarVisible = isTopBarVisible
            )
        }
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Text(text = "Loading...", modifier = modifier)
}

@Composable
private fun Catalog(
    favouriteMovieList: MovieList,
    filterList: FilterList,
    selectedFilterList: FilterList,
    onMovieClick: (movieId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    onSelectedFilterListUpdated: (FilterList) -> Unit,
    isTopBarVisible: Boolean,
) {
    val childPadding = rememberChildPadding()
    val filteredMoviesGridState = rememberLazyGridState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            filteredMoviesGridState.firstVisibleItemIndex == 0 &&
                    filteredMoviesGridState.firstVisibleItemScrollOffset < 20
        }
    }

    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }
    LaunchedEffect(isTopBarVisible) {
        if (isTopBarVisible) filteredMoviesGridState.animateScrollToItem(0)
    }

    val activity = LocalContext.current as Activity
    val currentOrientation = activity.resources.configuration.orientation

    Admob_interstitial.checkingCounter(activity)

    DisposableEffect(Unit) {
        val orientationEventListener = object : OrientationEventListener(activity) {
            private var isDelaying = false
            private val handler = Handler()

            override fun onOrientationChanged(orientation: Int) {
                if (!isDelaying) {
                    isDelaying = true
                    handler.postDelayed({
                        val newOrientation = when (orientation) {
                            in 45..134 -> {
                                ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                            }

                            in 135..224 -> {
                                ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                            }

                            in 225..314 -> {
                                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            }

                            else -> {
                                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            }
                        }
                        if (currentOrientation != newOrientation) {
                            activity.requestedOrientation = newOrientation
                        }

                        isDelaying = false
                    }, 500)
                }
            }
        }

        orientationEventListener.enable()

        onDispose {
            orientationEventListener.disable()
        }
    }

    val chipRowTopPadding by animateDpAsState(
        targetValue = if (shouldShowTopBar) 0.dp else childPadding.top, label = ""
    )

    Column {
            MovieFilterChipRow(
                filterList = filterList,
                selectedFilterList = selectedFilterList,
                modifier = Modifier.padding(top = chipRowTopPadding),
                onSelectedFilterListUpdated = onSelectedFilterListUpdated
            )
            FilteredMoviesGrid(
                state = filteredMoviesGridState,
                movieList = favouriteMovieList,
                onMovieClick = onMovieClick
            )
        }

}
