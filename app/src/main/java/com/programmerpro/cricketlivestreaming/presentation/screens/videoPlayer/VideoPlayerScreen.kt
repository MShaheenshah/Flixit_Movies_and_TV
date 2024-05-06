package com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Handler
import android.view.OrientationEventListener
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.programmerpro.cricketlivestreaming.data.entities.MovieDetails
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import com.programmerpro.cricketlivestreaming.must.ads.Admob_interstitial2
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerControlsIcon
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerMainFrame
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerMediaTitle
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerMediaTitleType
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerOverlay
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerPulse
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.BACK
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.FORWARD
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerPulseState
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerSeeker
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.VideoPlayerState
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.rememberVideoPlayerPulseState
import com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer.components.rememberVideoPlayerState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

object VideoPlayerScreen {
    const val MovieIdBundleKey = "movieId"
}

@OptIn(UnstableApi::class) @RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
fun VideoPlayerScreen(
    onBackPressed: () -> Unit,
    videoPlayerScreenViewModel: VideoPlayerScreenViewModel = hiltViewModel(),
) {
    val uiState by videoPlayerScreenViewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        is VideoPlayerScreenUiState.Loading -> {}
        is VideoPlayerScreenUiState.Error -> {}
        is VideoPlayerScreenUiState.Done -> {
            VideoPlayerScreenContent(
                movieDetails = s.movieDetails,
                onBackPressed = onBackPressed,
                videoPlayerScreenViewModel = videoPlayerScreenViewModel
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N_MR1)
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreenContent(
    movieDetails: MovieDetails,
    onBackPressed: () -> Unit,
    videoPlayerScreenViewModel: VideoPlayerScreenViewModel
) {
    val context = LocalContext.current
    val videoPlayerState = rememberVideoPlayerState(hideSeconds = 4)

    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    val activity = LocalContext.current as Activity
    val currentOrientation = activity.resources.configuration.orientation

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

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
            lifecycleOwner.lifecycle.removeObserver(observer)
            orientationEventListener.disable()
        }
    }

    var contentCurrentPosition by remember { mutableLongStateOf(0L) }
    var isPlaying: Boolean by remember { mutableStateOf(videoPlayerScreenViewModel.isPlaying.value) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(300)
            contentCurrentPosition = videoPlayerScreenViewModel.contentCurrentPosition.value
            isPlaying = videoPlayerScreenViewModel.isPlaying.value
        }
    }
    val exoPlayer: Player = videoPlayerScreenViewModel.exoPlayerInstance

//    Admob_interstitial2.loadAd(activity, exoPlayer)

    val adCountdownEnabled = videoPlayerScreenViewModel.adCountdownEnabled
    val countdownTime = videoPlayerScreenViewModel.countdownTime

    BackHandler(onBack = onBackPressed)

    val pulseState = rememberVideoPlayerPulseState()
    val screenWidth = with(LocalContext.current) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        point.x
    }

    Box(
        modifier = Modifier
            .handleTouchEvents(
                exoPlayer,
                videoPlayerState,
                pulseState,
                screenWidth,
            )
            .focusable()
    ){
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PlayerView(context)
                    .also {
                        it.player = exoPlayer
                    }
                    .apply { useController = false }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                        it.player?.pause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }
                    Lifecycle.Event.ON_CREATE -> {
                        it.onResume()
                    }
                    Lifecycle.Event.ON_START -> {
                        it.onResume()
                    }
                    else -> Unit
                }
            },
        )

        if (adCountdownEnabled.value && countdownTime.value > 0) {
            Admob_interstitial2.loadAd(activity, exoPlayer)
            Text(
                text = "loading ad...",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { /* Handle click if needed */ },
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            Spacer(modifier = Modifier.size(0.dp)) // Hide the text by using a Spacer with zero size
        }

        val focusRequester = remember { FocusRequester() }
        VideoPlayerOverlay(
            modifier = Modifier.align(Alignment.BottomCenter),
            focusRequester = focusRequester,
            state = videoPlayerState,
            isPlaying = isPlaying,
            centerButton = { VideoPlayerPulse(pulseState) },
            subtitles = { /* TODO Implement subtitles */ },
            controls = {
                VideoPlayerControls(
                    movieDetails,
                    isPlaying,
                    contentCurrentPosition,
                    exoPlayer,
                    videoPlayerState,
                    focusRequester,
                    { videoPlayerScreenViewModel.bufferedPercentage.value },
                    { videoPlayerScreenViewModel.totalDuration.value },
                    { videoPlayerScreenViewModel.currentTime.value },
                ) { timeMs: Float ->
                    videoPlayerScreenViewModel.seekTo(timeMs.toLong())
                }
            }
        )
    }
}

@SuppressLint("SourceLockedOrientationActivity")
@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
fun VideoPlayerControls(
    movieDetails: MovieDetails,
    isPlaying: Boolean,
    contentCurrentPosition: Long,
    exoPlayer: Player,
    state: VideoPlayerState,
    focusRequester: FocusRequester,
    bufferedPercentage: () -> Int,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    onSeekChanged: (Float) -> Unit
) {
    val onPlayPauseToggle = { shouldPlay: Boolean ->
        if (shouldPlay) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    VideoPlayerMainFrame(
        mediaTitle = {
            VideoPlayerMediaTitle(
                title = movieDetails.name,
                secondaryText = movieDetails.releaseDate,
                tertiaryText = movieDetails.director,
                type = VideoPlayerMediaTitleType.DEFAULT
            )
        },
        mediaActions = {
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val activity = LocalContext.current as Activity
                val currentOrientation = activity.resources.configuration.orientation

                VideoPlayerControlsIcon(
                    icon = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Icons.Default.FullscreenExit
                    } else {
                        Icons.Default.Fullscreen
                    },
                    state = state,
                    isPlaying = isPlaying,
                    contentDescription = StringConstants
                        .Composable
                        .VideoPlayerControlPlayFullScreenButton,
                    onClick = {
                        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        } else {
                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        }
                    }
                )
//                VideoPlayerControlsIcon(
//                    modifier = Modifier.padding(start = 12.dp),
//                    icon = Icons.Default.ClosedCaption,
//                    state = state,
//                    isPlaying = isPlaying,
//                    contentDescription = StringConstants
//                        .Composable
//                        .VideoPlayerControlClosedCaptionsButton
//                )
//                VideoPlayerControlsIcon(
//                    modifier = Modifier.padding(start = 12.dp),
//                    icon = Icons.Default.Settings,
//                    state = state,
//                    isPlaying = isPlaying,
//                    contentDescription = StringConstants
//                        .Composable
//                        .VideoPlayerControlSettingsButton
//                )
            }
        },
        seeker = {
            VideoPlayerSeeker(
                focusRequester,
                state,
                isPlaying,
                onPlayPauseToggle,
                contentProgress = contentCurrentPosition.milliseconds,
                contentDuration = exoPlayer.duration.milliseconds,
                bufferedPercentage = bufferedPercentage,
                onSeekChanged = onSeekChanged,
                totalDuration = totalDuration,
                currentTime = currentTime,
            )
        },
        more = null
    )
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@RequiresApi(Build.VERSION_CODES.N_MR1)
private fun Modifier.handleTouchEvents(
    exoPlayer: Player,
    videoPlayerState: VideoPlayerState,
    pulseState: VideoPlayerPulseState,
    screenWidth: Int,
): Modifier = pointerInput(Unit){

    val onLeft = {
        val seekIncrement = 10000L
        val newPosition = exoPlayer.currentPosition - seekIncrement
        exoPlayer.seekTo(newPosition.coerceAtLeast(0L))
        pulseState.setType(BACK)
    }
    val onRight = {
        val seekIncrement = 10000L
        val newPosition = exoPlayer.currentPosition + seekIncrement
        exoPlayer.seekTo(newPosition.coerceAtMost(exoPlayer.duration))
        pulseState.setType(FORWARD)
    }
    val onTap = {
        if (videoPlayerState.controlsVisible) videoPlayerState.hideControls()
        else videoPlayerState.showControls()
    }
    detectTapGestures (
        onTap = {
            onTap()
        },
        onDoubleTap = {offset ->
            val tapX = offset.x

            val isLeftSide = tapX < screenWidth / 2

            if (isLeftSide) {
                onLeft()
            } else {
                onRight()
            }
        },
    )
}