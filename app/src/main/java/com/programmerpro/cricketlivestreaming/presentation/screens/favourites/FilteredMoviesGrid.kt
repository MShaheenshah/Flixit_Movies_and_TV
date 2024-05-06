package com.programmerpro.cricketlivestreaming.presentation.screens.favourites

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.programmerpro.cricketlivestreaming.data.entities.MovieList
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamBorderWidth
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamBottomListPadding
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamCardShape
import com.programmerpro.cricketlivestreaming.presentation.utils.focusOnInitialVisibility

@Composable
fun FilteredMoviesGrid(
    state: LazyGridState,
    movieList: MovieList,
    onMovieClick: (movieId: String) -> Unit,
) {
    val isFirstItemVisible = remember { mutableStateOf(false) }
    LazyVerticalGrid(
        state = state,
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(4),
    ) {
        movieList.forEachIndexed { index, movie ->
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
                            .aspectRatio(11 / 16f)
                            .padding(8.dp)
                            .scale(1f)
                            .clickable { onMovieClick(movie.id) }
                            .then(
                                if (index == 0)
                                    Modifier.focusOnInitialVisibility(isFirstItemVisible)
                                else Modifier
                            ),
                        shape = JetStreamCardShape,
                    ){
                        Box(
                            modifier = Modifier.fillMaxSize()
                        )  {
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
                            Text(
                                text = movie.name,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    shadow = Shadow(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        offset = Offset(x = 2f, y = 4f),
                                        blurRadius = 2f
                                    )
                                ),
                                maxLines = 3,
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.5f)) // Apply a translucent white background
                                    .padding(horizontal = 8.dp, vertical = 4.dp) // Add some padding for better visibility
                            )
                        }
                    }
                }
            }
        }
        item(span = { GridItemSpan(currentLineSpan = 5) }) {
            Spacer(modifier = Modifier.padding(bottom = JetStreamBottomListPadding))
        }
    }
}
