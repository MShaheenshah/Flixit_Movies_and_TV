package com.programmerpro.cricketlivestreaming.presentation.screens.categories

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Handler
import android.view.OrientationEventListener
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.programmerpro.cricketlivestreaming.data.entities.Movie
import com.programmerpro.cricketlivestreaming.data.entities.MovieCategoryDetails
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import com.programmerpro.cricketlivestreaming.must.ads.Admob_interstitial
import com.programmerpro.cricketlivestreaming.presentation.common.rememberChildPadding
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamBorderWidth
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamBottomListPadding
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamCardShape
import com.programmerpro.cricketlivestreaming.presentation.utils.focusOnInitialVisibility

object CategoryMovieListScreen {
    const val CategoryIdBundleKey = "categoryId"
}

@Composable
fun CategoryMovieListScreen(
    onBackPressed: () -> Unit,
    onMovieSelected: (Movie) -> Unit,
    categoryMovieListScreenViewModel: CategoryMovieListScreenViewModel = hiltViewModel()
) {
    val uiState by categoryMovieListScreenViewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        is CategoryMovieListScreenUiState.Loading -> {
            Loading()
        }
        is CategoryMovieListScreenUiState.Error -> {
            Error()
        }
        is CategoryMovieListScreenUiState.Done -> {
            val categoryDetails = s.movieCategoryDetails
            CategoryDetails(
                categoryDetails = categoryDetails,
                onBackPressed = onBackPressed,
                onMovieSelected = onMovieSelected
            )
        }
    }
}

@Composable
private fun CategoryDetails(
    categoryDetails: MovieCategoryDetails,
    onBackPressed: () -> Unit,
    onMovieSelected: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    val childPadding = rememberChildPadding()
    val isFirstItemVisible = remember { mutableStateOf(false) }

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = categoryDetails.name,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(
                vertical = childPadding.top.times(2.5f)
            )
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4)
        ) {
            categoryDetails.movies.forEachIndexed { index, movie ->
                item {
                    key(movie.id) {
                        Card(
                            modifier = Modifier
                                .border(
                                    border = BorderStroke(
                                        width = JetStreamBorderWidth,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                )
                                .aspectRatio(9 / 16f)
                                .padding(8.dp)
                                .scale(1f)
                                .clickable { onMovieSelected(movie) }
                                .then(
                                    if (index == 0)
                                        Modifier.focusOnInitialVisibility(isFirstItemVisible)
                                    else Modifier
                                ),
                            shape = JetStreamCardShape,
                        ){
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(movie.posterUri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = StringConstants
                                    .Composable
                                    .ContentDescription
                                    .moviePoster(movie.name),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
            item(span = { GridItemSpan(currentLineSpan = 5) }) {
                Spacer(modifier = Modifier.padding(bottom = JetStreamBottomListPadding))
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
    Text(text = "Wops, something went wrong...", modifier = modifier)
}