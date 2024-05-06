package com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import kotlin.time.Duration

@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
fun VideoPlayerSeeker(
    focusRequester: FocusRequester,
    state: VideoPlayerState,
    isPlaying: Boolean,
    onPlayPauseToggle: (Boolean) -> Unit,
    contentProgress: Duration,
    contentDuration: Duration,
    bufferedPercentage: () -> Int,
    onSeekChanged: (Float) -> Unit,
    totalDuration: () -> Long,
    currentTime: () -> Long,
) {
    val contentProgressString =
        contentProgress.toComponents { h, m, s, _ ->
            if(h > 0) {
                "$h:${m.padStartWith0()}:${s.padStartWith0()}"
            } else {
                ":${m.padStartWith0()}:${s.padStartWith0()}"
            }

        }
    val contentDurationString =
        contentDuration.toComponents { h, m, s, _ ->
            if(h > 0) {
                "$h:${m.padStartWith0()}:${s.padStartWith0()}"
            } else {
                ":${m.padStartWith0()}:${s.padStartWith0()}"
            }

        }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        VideoPlayerControlsIcon(
            modifier = Modifier.focusRequester(focusRequester),
            icon = if (!isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
            onClick = { onPlayPauseToggle(!isPlaying) },
            state = state,
            isPlaying = isPlaying,
            contentDescription = StringConstants
                .Composable
                .VideoPlayerControlPlayPauseButton
        )
        VideoPlayerControllerText(text = contentProgressString)
        VideoPlayerControllerIndicator(
            bufferedPercentage = bufferedPercentage,
            onSeekChanged = onSeekChanged,
            totalDuration = totalDuration,
            currentTime = currentTime,
            state = state,
        )
        VideoPlayerControllerText(text = contentDurationString)
    }
}

private fun Number.padStartWith0() = this.toString().padStart(2, '0')
