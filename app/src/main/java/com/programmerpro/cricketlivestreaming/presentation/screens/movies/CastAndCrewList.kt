package com.programmerpro.cricketlivestreaming.presentation.screens.movies

import android.media.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.programmerpro.cricketlivestreaming.R
import com.programmerpro.cricketlivestreaming.data.entities.MovieCast
import com.programmerpro.cricketlivestreaming.presentation.common.rememberChildPadding
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamBorderWidth
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamCardShape
import com.programmerpro.cricketlivestreaming.presentation.utils.ourColors

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CastAndCrewList(castAndCrew: List<MovieCast>) {
    val childPadding = rememberChildPadding()

    Column(
        modifier = Modifier.padding(top = childPadding.top)
    ) {
        Text(
            text = stringResource(R.string.cast_and_crew),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(start = childPadding.start)
        )
        LazyRow(
            modifier = Modifier
                .padding(top = 16.dp)
                .focusRestorer()
                .offset(y = 0.07f.dp),
        ) {
            item { Spacer(modifier = Modifier.padding(start = childPadding.start)) }

            items(castAndCrew, key = { it.id }) {
                CastAndCrewItem(it, modifier = Modifier.width(144.dp))
            }
        }
    }
}

@Composable
private fun CastAndCrewItem(
    castMember: MovieCast,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 20.dp, bottom = 16.dp)
            .aspectRatio(1 / 1.8f)
            .scale(
                scale = 1f
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp),
        shape = JetStreamCardShape,
        border = BorderStroke(
            width = JetStreamBorderWidth,
            color = MaterialTheme.colorScheme.onSurface
        )
    ){
        Column(modifier = modifier.padding(8.dp)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(horizontal = 12.dp),
                text = castMember.realName,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = castMember.characterName,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .alpha(0.75f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.725f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(castMember.avatarUrl)
                        .crossfade(true).build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
