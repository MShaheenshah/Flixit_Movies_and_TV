package com.programmerpro.cricketlivestreaming.presentation.screens.search

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Handler
import android.view.OrientationEventListener
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programmerpro.cricketlivestreaming.R
import com.programmerpro.cricketlivestreaming.data.entities.Movie
import com.programmerpro.cricketlivestreaming.data.entities.MovieList
import com.programmerpro.cricketlivestreaming.must.ads.Admob_interstitial
import com.programmerpro.cricketlivestreaming.presentation.common.rememberChildPadding
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamCardShape

@Composable
fun SearchScreen(
    onMovieClick: (movie: Movie) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel(),
) {
    val tvLazyColumnState = rememberLazyListState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            tvLazyColumnState.firstVisibleItemIndex == 0 &&
                    tvLazyColumnState.firstVisibleItemScrollOffset < 20
        }
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

    val searchState by searchScreenViewModel.searchState.collectAsStateWithLifecycle()

    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }

    when (val s = searchState) {
        is SearchState.Searching -> {
            Text(text = "Searching...")
        }

        is SearchState.Done -> {
            val movieList = s.movieList
            SearchResult(
                movieList = movieList,
                searchMovies = searchScreenViewModel::query,
                onMovieClick = onMovieClick
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResult(
    movieList: MovieList,
    searchMovies: (queryString: String) -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
    tvLazyColumnState: LazyListState = rememberLazyListState(),
) {
    val childPadding = rememberChildPadding()
    var searchQuery by remember { mutableStateOf("") }
    val tfFocusRequester = remember { FocusRequester() }
    val tfInteractionSource = remember { MutableInteractionSource() }

    val isTfFocused by tfInteractionSource.collectIsFocusedAsState()
    LazyColumn(
        modifier = modifier,
        state = tvLazyColumnState
    ) {
        item {
            Surface(
                modifier = modifier
                    .padding(horizontal = childPadding.start)
                    .padding(top = 8.dp)
                    .scale(1f),
                shape = JetStreamCardShape,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    width = if (isTfFocused) 2.dp else 1.dp,
                    color = animateColorAsState(
                        targetValue = if (isTfFocused) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface, label = ""
                    ).value
                ),
                tonalElevation = 2.dp,
                onClick = { tfFocusRequester.requestFocus() }
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = {
                        updatedQuery -> searchQuery = updatedQuery
                        searchMovies(updatedQuery)
                                    },
                    decorationBox = {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .padding(start = 20.dp),
                        ) {
                            it()
                            if (searchQuery.isEmpty()) {
                                Text(
                                    modifier = Modifier.graphicsLayer { alpha = 0.6f },
                                    text = stringResource(R.string.search_screen_et_placeholder),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        )
                        .focusRequester(tfFocusRequester),
                    cursorBrush = Brush.verticalGradient(
                        colors = listOf(
                            LocalContentColor.current,
                            LocalContentColor.current,
                        )
                    ),
                    maxLines = 1,
                    interactionSource = tfInteractionSource,
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        item {
            SearchMoviesRow(
                movies = movieList,
                onMovieClick = onMovieClick
            )
        }
    }
}
