package com.programmerpro.cricketlivestreaming.presentation.screens.movies

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Handler
import android.view.OrientationEventListener
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programmerpro.cricketlivestreaming.R
import com.programmerpro.cricketlivestreaming.data.entities.Movie
import com.programmerpro.cricketlivestreaming.data.entities.MovieDetails
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import com.programmerpro.cricketlivestreaming.must.ads.Admob_interstitial
import com.programmerpro.cricketlivestreaming.presentation.common.MoviesRow
import com.programmerpro.cricketlivestreaming.presentation.common.rememberChildPadding

object MovieDetailsScreen {
    const val MovieIdBundleKey = "movieId"
}

@Composable
fun MovieDetailsScreen(
    goToMoviePlayer: (movieId: String) -> Unit,
    onBackPressed: () -> Unit,
    refreshScreenWithNewMovie: (Movie) -> Unit,
    movieDetailsScreenViewModel: MovieDetailsScreenViewModel = hiltViewModel()
) {
    val uiState by movieDetailsScreenViewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        is MovieDetailsScreenUiState.Loading -> {
            Loading()
        }

        is MovieDetailsScreenUiState.Error -> {
            Error()
        }

        is MovieDetailsScreenUiState.Done -> {
            Details(
                movieDetails = s.movieDetails,
                goToMoviePlayer = goToMoviePlayer,
                onBackPressed = onBackPressed,
                refreshScreenWithNewMovie = refreshScreenWithNewMovie,
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize()
            )
        }
    }
}

@Composable
private fun Details(
    movieDetails: MovieDetails,
    goToMoviePlayer: (movieId: String) -> Unit,
    onBackPressed: () -> Unit,
    refreshScreenWithNewMovie: (Movie) -> Unit,
    modifier: Modifier = Modifier,
) {
    val childPadding = rememberChildPadding()

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

    BackHandler(onBack = onBackPressed)
    LazyColumn(
        contentPadding = PaddingValues(bottom = 50.dp),
        modifier = modifier,
    ) {
        item {
            MovieDetails(
                movieDetails = movieDetails,
                goToMoviePlayer = goToMoviePlayer
            )
        }

//        item {
//            CastAndCrewList(
//                castAndCrew = movieDetails.castAndCrew
//            )
//        }

        item {
            MoviesRow(
                title = StringConstants
                    .Composable
                    .movieDetailsScreenSimilarTo(movieDetails.name),
                titleStyle = MaterialTheme.typography.titleMedium,
                movies = movieDetails.similarMovies,
                onMovieClick = refreshScreenWithNewMovie
            )
        }

//        item {
//            MovieReviews(
//                modifier = Modifier.padding(top = childPadding.top),
//                reviewsAndRatings = movieDetails.reviewsAndRatings
//            )
//        }

        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = childPadding.start)
                    .padding(BottomDividerPadding)
                    .fillMaxWidth()
                    .height(1.dp)
                    .alpha(0.15f)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        }

        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = childPadding.start),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val itemModifier = Modifier.width(192.dp)

                    TitleValueText(
                        modifier = itemModifier,
                        title = stringResource(R.string.status),
                        value = movieDetails.status
                    )
                    TitleValueText(
                        modifier = itemModifier,
                        title = stringResource(R.string.original_language),
                        value = movieDetails.originalLanguage
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = childPadding.start),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val itemModifier = Modifier.width(192.dp)

                    TitleValueText(
                        modifier = itemModifier,
                        title = stringResource(R.string.budget),
                        value = movieDetails.budget
                    )
                    TitleValueText(
                        modifier = itemModifier,
                        title = stringResource(R.string.revenue),
                        value = movieDetails.revenue
                    )
                }
            }
        }
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Text(text = "Loading...", modifier = modifier)
}

@Composable
private fun Error(modifier: Modifier = Modifier) {
    Text(text = "Something went wrong...", modifier = modifier)
}

private val BottomDividerPadding = PaddingValues(vertical = 16.dp)