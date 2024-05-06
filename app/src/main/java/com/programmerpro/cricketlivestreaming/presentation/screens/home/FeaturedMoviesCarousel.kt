package com.programmerpro.cricketlivestreaming.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.programmerpro.cricketlivestreaming.R
import com.programmerpro.cricketlivestreaming.data.entities.Movie
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamBorderWidth
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamButtonShape
import com.programmerpro.cricketlivestreaming.presentation.utils.Padding
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.N_MR1)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedMoviesCarousel(
    movies: List<Movie>,
    padding: Padding,
    goToVideoPlayer: (movieId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val carouselState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0.0f,
        pageCount = { movies.size },
    )
    val isCarouselFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        HorizontalPager(
            beyondBoundsPageCount = movies.size,
            pageSpacing = 5.dp,
            contentPadding = PaddingValues(horizontal = 5.dp),
            state = carouselState,
            modifier = modifier
                .padding(start = padding.start, end = padding.start, top = padding.top)
                .border(
                    width = JetStreamBorderWidth,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isCarouselFocused) 1f else 0f),
                    shape = ShapeDefaults.Medium,
                )
                .clip(ShapeDefaults.Medium)
                .semantics {
                    contentDescription =
                        StringConstants.Composable.ContentDescription.MoviesCarousel
                },
        ) { index ->
            val pageOffset =
                (carouselState.currentPage - index) + carouselState.currentPageOffsetFraction
            val foregroundSize by animateFloatAsState(
                targetValue = if (pageOffset != 0.0f) 0.75f else 1f,
                animationSpec = tween(durationMillis = 300),
                label = ""
            )
            val movie = movies[index]
            // background
            CarouselItemForeground(
                movie = movie,
                modifier = Modifier.fillMaxSize(),
                animate = foregroundSize,
                videoPlayer = goToVideoPlayer
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        CarouselIndicator(
            itemCount = movies.size,
            currentPage = carouselState.currentPage,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
    LaunchedEffect(carouselState.currentPage){
        launch {
            while (true){
                delay(5000)
                withContext(NonCancellable){
                    val nextPage = carouselState.currentPage + 1
                    if (nextPage < carouselState.pageCount) {
                        carouselState.animateScrollToPage(nextPage)
                    } else {
                        val initPage = 0
                        carouselState.animateScrollToPage(initPage)
                    }
                }
            }
        }
    }
}

@Composable
fun CarouselIndicator(
    itemCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        repeat(itemCount) { index ->
            val color = if (index == currentPage) Color.Black else Color.Gray
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
private fun CarouselItemForeground(
    movie: Movie,
    modifier: Modifier = Modifier,
    animate: Float,
    videoPlayer: (movieId: String) -> Unit,
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = animate
                scaleY = animate
            },
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = movie.posterUri,
            contentDescription = StringConstants
                .Composable
                .ContentDescription
                .moviePoster(movie.name),
            modifier = modifier
                .drawWithContent {
                    drawContent()
                    drawRect(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f)
                            )
                        )
                    )
                },
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = movie.name,
                style = MaterialTheme.typography.titleSmall.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 2f, y = 4f),
                        blurRadius = 2f
                    )
                ),
                maxLines = 1,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f)) // Apply a translucent white background
                    .padding(horizontal = 8.dp, vertical = 4.dp) // Add some padding for better visibility
            )
            Button(
                onClick = { videoPlayer(movie.id) },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .scale(scale = 1f),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                shape = JetStreamButtonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    contentColor = MaterialTheme.colorScheme.surface,
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.surface // Add this line to apply tint color
                    )
                    Spacer(Modifier.width(8.dp)) // Use width() instead of size() to specify horizontal spacing
                    Text(
                        text = stringResource(R.string.watch_now),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}
