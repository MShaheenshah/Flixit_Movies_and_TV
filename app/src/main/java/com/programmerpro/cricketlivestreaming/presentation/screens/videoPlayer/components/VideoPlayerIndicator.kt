package com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components

import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.VideoPlayerScreenViewModel

@OptIn(UnstableApi::class) @RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
fun RowScope.VideoPlayerControllerIndicator(
    bufferedPercentage: () -> Int,
    onSeekChanged: (Float) -> Unit,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    state: VideoPlayerState,
) {

    val videoPlayerScreenViewModel: VideoPlayerScreenViewModel = hiltViewModel()
    val interactionSource = remember { MutableInteractionSource() }
    val isSelected by remember { mutableStateOf(false) }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val color by rememberUpdatedState(
        newValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurface
    )
    val animatedIndicatorHeight by animateDpAsState(
        targetValue = 4.dp.times((if (isFocused) 2.5f else 1f)), label = ""
    )

    val buffer = remember(bufferedPercentage()) { bufferedPercentage() }
    val duration = remember(totalDuration()) { totalDuration() }
    val videoTime = remember(currentTime()) { currentTime() }

    LaunchedEffect(isSelected) {
        if (isSelected) {
            state.showControls(seconds = Int.MAX_VALUE)
        } else {
            state.showControls()
        }
    }
    Canvas(
        modifier = Modifier
            .weight(1f)
            .height(animatedIndicatorHeight)
            .padding(horizontal = 4.dp)
            .pointerInput(Unit){
                 detectTapGestures(
                    onTap = {offset ->
                        val seekPosition = offset.x / size.width
                        onSeekChanged(seekPosition)
                        val durations = totalDuration()
                        val timeMs = (seekPosition * durations).toLong()
                        videoPlayerScreenViewModel.seekTo(timeMs)
                    }
                 )
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        val seekPosition = change.positionChange().x / size.width
                        val seekDelta = dragAmount.x / size.width
                        val newSeekPosition = (seekPosition + seekDelta).coerceIn(0f, 1f)
                        onSeekChanged(newSeekPosition)
                        val durations = totalDuration()
                        val timeMs = (newSeekPosition * durations).toLong()
                        videoPlayerScreenViewModel.seekTo(timeMs)
                    }
                )
            }
            .focusable(interactionSource = interactionSource),
        onDraw = {
            val yOffset = size.height.div(2)
            val bufferPosition = buffer.toFloat() / 100f
            val seekPosition = videoTime.toFloat() / duration.toFloat()
            drawLine(
                color = color.copy(alpha = 0.24f),
                start = Offset(x = 0f, y = yOffset),
                end = Offset(x = size.width, y = yOffset),
                strokeWidth = size.height,
                cap = StrokeCap.Round
            )
            drawLine(
                color = color.copy(alpha = 0.5f),
                start = Offset(x = 0f, y = yOffset),
                end = Offset(x = size.width * bufferPosition, y = yOffset),
                strokeWidth = size.height,
                cap = StrokeCap.Round
            )

            // Draw seek line
            drawLine(
                color = color,
                start = Offset(x = 0f, y = yOffset),
                end = Offset(x = size.width * seekPosition, y = yOffset),
                strokeWidth = size.height,
                cap = StrokeCap.Round
            )

            val seekRadius = 8.dp.toPx() // Adjust the radius as needed
            val seekX = size.width * seekPosition
            drawCircle(
                color = color,
                radius = seekRadius,
                center = Offset(x = seekX, y = yOffset)
            )
        }
    )
}
