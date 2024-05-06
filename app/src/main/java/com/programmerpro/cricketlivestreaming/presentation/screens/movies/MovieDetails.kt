package com.programmerpro.cricketlivestreaming.presentation.screens.movies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.programmerpro.cricketlivestreaming.R
import com.programmerpro.cricketlivestreaming.data.entities.MovieDetails
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import com.programmerpro.cricketlivestreaming.presentation.common.rememberChildPadding
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamButtonShape
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieDetails(
    movieDetails: MovieDetails,
    goToMoviePlayer: (movieId: String) -> Unit
) {
    val childPadding = rememberChildPadding()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(472.dp)
    ) {
        MovieImageWithGradients(
            movieDetails = movieDetails,
            bringIntoViewRequester = bringIntoViewRequester
        )

        Column(modifier = Modifier.fillMaxWidth(0.55f)) {
            Spacer(modifier = Modifier.height(108.dp))
            Column(
                modifier = Modifier.padding(start = childPadding.start)
            ) {
                MovieLargeTitle(movieTitle = movieDetails.name)

                Column(
                    modifier = Modifier.alpha(0.75f).weight(1f)
                ) {
                    MovieDescription(description = movieDetails.description)
                    DotSeparatedRow(
                        modifier = Modifier
                            .padding(top = 20.dp),
                        texts = listOf(
                            movieDetails.pgRating,
                            movieDetails.releaseDate,
                            movieDetails.categories.joinToString(", "),
                            movieDetails.duration
                        )
                    )
                    DirectorScreenplayMusicRow(
                        director = movieDetails.director,
                    )
                }
                WatchTrailerButton(
                    modifier = Modifier.pointerInput(Unit){
                        detectTapGestures(
                            onTap = {
                                coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                            }
                        )
                    },
                    goToMoviePlayer = goToMoviePlayer,
                    movieDetails = movieDetails
                )
            }
        }
    }
}


@Composable
private fun WatchTrailerButton(
    modifier: Modifier = Modifier,
    goToMoviePlayer: (movieId: String) -> Unit,
    movieDetails: MovieDetails
) {
    Button(
        onClick = { goToMoviePlayer(movieDetails.id) },
        modifier = modifier.padding(top = 24.dp),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        shape = JetStreamButtonShape
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.watch_now),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun DirectorScreenplayMusicRow(
    director: String,
) {
    Row(modifier = Modifier
        .padding(top = 22.dp)
    ) {
        TitleValueText(
            modifier = Modifier
                .padding(end = 15.dp)
                .weight(1f),
            title = stringResource(R.string.director),
            value = director
        )
    }
}

@Composable
private fun MovieDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier.padding(top = 8.dp),
        maxLines = 2
    )
}

@Composable
private fun MovieLargeTitle(movieTitle: String) {
    Text(
        text = movieTitle,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold
        ),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieImageWithGradients(
    movieDetails: MovieDetails,
    bringIntoViewRequester: BringIntoViewRequester
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(movieDetails.posterUri)
            .crossfade(true).build(),
        contentDescription = StringConstants
            .Composable
            .ContentDescription
            .moviePoster(movieDetails.name),
        modifier = Modifier
            .fillMaxSize()
            .bringIntoViewRequester(bringIntoViewRequester),
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.surface
                    ),
                    startY = 600f
                )
            )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        Color.Transparent
                    ),
                    endX = 1000f,
                    startX = 300f
                )
            )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        Color.Transparent
                    ),
                    start = Offset(x = 500f, y = 500f),
                    end = Offset(x = 1000f, y = 0f)
                )
            )
    )
}
