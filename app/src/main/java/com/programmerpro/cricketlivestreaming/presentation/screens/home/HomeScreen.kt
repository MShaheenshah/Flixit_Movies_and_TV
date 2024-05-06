package com.programmerpro.cricketlivestreaming.presentation.screens.home

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Handler
import android.view.OrientationEventListener
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programmerpro.cricketlivestreaming.data.entities.Movie
import com.programmerpro.cricketlivestreaming.data.entities.MovieList
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import com.programmerpro.cricketlivestreaming.must.ads.Admob_interstitial
import com.programmerpro.cricketlivestreaming.presentation.common.MoviesRow
import com.programmerpro.cricketlivestreaming.presentation.common.rememberChildPadding

@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
fun HomeScreen(
    onMovieClick: (movie: Movie) -> Unit,
    goToVideoPlayer: (movieId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    isTopBarVisible: Boolean,
    homeScreeViewModel: HomeScreeViewModel = hiltViewModel(),
) {
    val uiState by homeScreeViewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        is HomeScreenUiState.Ready -> {
            CheckData(
                featuredMovies = s.featuredMovieList,
                trendingMovies = s.trendingMovieList,
                top10Movies = s.top10MovieList,
                nowPlayingMovies = s.nowPlayingMovieList,
                onMovieClick = onMovieClick,
                onScroll = onScroll,
                goToVideoPlayer = goToVideoPlayer,
                isTopBarVisible = isTopBarVisible,
                modifier = Modifier.fillMaxSize(),
            )
        }
        is HomeScreenUiState.Loading -> Loading()
        is HomeScreenUiState.Error -> Error()
    }
}

@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
fun CheckData(
    featuredMovies: MovieList,
    trendingMovies: MovieList,
    top10Movies: MovieList,
    nowPlayingMovies: MovieList,
    onMovieClick: (movie: Movie) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    goToVideoPlayer: (movieId: String) -> Unit,
    isTopBarVisible: Boolean,
    modifier: Modifier,
) {
    if (featuredMovies.isEmpty()){
        RefreshWindow()
    }else{
        Catalog(
            featuredMovies = featuredMovies,
            trendingMovies = trendingMovies,
            top10Movies = top10Movies,
            nowPlayingMovies = nowPlayingMovies,
            onMovieClick = onMovieClick,
            onScroll = onScroll,
            goToVideoPlayer = goToVideoPlayer,
            isTopBarVisible = isTopBarVisible,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
private fun RefreshWindow(
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Network Error")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {  }) {
                Text(text = "Close the app and reopen it...")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
private fun Catalog(
    featuredMovies: MovieList,
    trendingMovies: MovieList,
    top10Movies: MovieList,
    nowPlayingMovies: MovieList,
    onMovieClick: (movie: Movie) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    goToVideoPlayer: (movieId: String) -> Unit,
    modifier: Modifier = Modifier,
    isTopBarVisible: Boolean = true,
) {

    val tvLazyListState = rememberLazyListState()
    val childPadding = rememberChildPadding()

    val shouldShowTopBar by remember {
        derivedStateOf {
            tvLazyListState.firstVisibleItemIndex == 0 &&
                    tvLazyListState.firstVisibleItemScrollOffset < 30
        }
    }

    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }
    LaunchedEffect(isTopBarVisible) {
        if (isTopBarVisible) tvLazyListState.animateScrollToItem(0)
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

    LazyColumn(
        modifier = modifier,
        state = tvLazyListState,
        contentPadding = PaddingValues(bottom = 10.dp),
        userScrollEnabled = true
        // Setting overscan margin to bottom to ensure the last row's visibility
    ) {
        item(contentType = "FeaturedMoviesCarousel") {
            FeaturedMoviesCarousel(
                movies = featuredMovies,
                padding = childPadding,
                goToVideoPlayer = goToVideoPlayer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }
        item(contentType = "MoviesRow") {
            MoviesRow(
                modifier = Modifier.padding(top = 16.dp),
                movies = trendingMovies,
                title = StringConstants.Composable.HomeScreenTrendingTitle,
                onMovieClick = onMovieClick
            )
        }
        item(contentType = "Top10MoviesList") {
            Top10MoviesList(
                moviesState = top10Movies,
                onMovieClick = onMovieClick
            )
        }
        item(contentType = "MoviesRow") {
            MoviesRow(
                modifier = Modifier.padding(top = 16.dp),
                movies = nowPlayingMovies,
                title = StringConstants.Composable.HomeScreenNowPlayingMoviesTitle,
                onMovieClick = onMovieClick
            )
        }
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Text(text = "Loading...", modifier = modifier)
}

@Composable
private fun Error(modifier: Modifier = Modifier) {
    Text(text = "Wops, something went wrong.", modifier = modifier)
}
