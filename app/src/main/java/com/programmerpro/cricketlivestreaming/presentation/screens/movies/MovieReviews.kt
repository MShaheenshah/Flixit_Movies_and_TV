package com.programmerpro.cricketlivestreaming.presentation.screens.movies

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.programmerpro.cricketlivestreaming.R
import com.programmerpro.cricketlivestreaming.data.entities.MovieReviewsAndRatings
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import com.programmerpro.cricketlivestreaming.presentation.common.rememberChildPadding
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamCardShape

@Composable
fun MovieReviews(
    modifier: Modifier = Modifier,
    reviewsAndRatings: List<MovieReviewsAndRatings>
) {
    val childPadding = rememberChildPadding()
    Column(
        modifier = modifier
            .padding(horizontal = childPadding.start)
            .padding(bottom = childPadding.bottom)
    ) {
        Text(text = stringResource(R.string.reviews), style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            reviewsAndRatings.forEach { reviewAndRating ->
                Review(
                    reviewAndRating,
                    modifier
                        .weight(1f)
                        .height(96.dp)
                )
            }
        }
    }
}

@Composable
private fun Review(
    reviewAndRating: MovieReviewsAndRatings,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 1.dp,
        modifier = modifier,
        border = BorderStroke(
            width = ReviewItemOutlineWidth,
            color = MaterialTheme.colorScheme.onSurface
        ),
        shape = JetStreamCardShape,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.3f)
                        .background(
                            MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(0.8f)
                            .align(Alignment.Center),
                    )
                }
                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = reviewAndRating.reviewerName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = StringConstants
                            .Composable
                            .reviewCount(reviewAndRating.reviewCount),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.alpha(0.75f)
                    )
                }
            }
            Text(
                text = reviewAndRating.reviewRating,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

private val ReviewItemOutlineWidth = 2.dp
