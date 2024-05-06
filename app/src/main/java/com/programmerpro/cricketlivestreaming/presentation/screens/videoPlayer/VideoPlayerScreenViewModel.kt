package com.programmerpro.cricketlivestreaming.presentation.screens.videoPlayer

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.programmerpro.cricketlivestreaming.data.entities.MovieDetails
import com.programmerpro.cricketlivestreaming.data.repositories.MovieRepository
import com.programmerpro.cricketlivestreaming.must.launcherActivity.SplashActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi @HiltViewModel
class VideoPlayerScreenViewModel @Inject constructor(
    private val exoPlayer: Player,
    savedStateHandle: SavedStateHandle,
    repository: MovieRepository,
    private val context: Context
) : ViewModel() {

    private var isPlayerPrepared = false

    private val _contentCurrentPosition = MutableStateFlow(0L)
    val contentCurrentPosition: StateFlow<Long> = _contentCurrentPosition

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    val exoPlayerInstance: Player
        get() = exoPlayer

    private val _uiState = MutableStateFlow<VideoPlayerScreenUiState>(VideoPlayerScreenUiState.Loading)
    val uiState: StateFlow<VideoPlayerScreenUiState> = _uiState.asStateFlow()

    private val _bufferedPercentage = mutableStateOf(0)
    val bufferedPercentage: State<Int> = _bufferedPercentage

    private val _totalDuration = mutableStateOf(0L)
    val totalDuration: State<Long> = _totalDuration

    private val _adCountdownEnabled = mutableStateOf(false)
    val adCountdownEnabled: State<Boolean> = _adCountdownEnabled

    private val _countdownTime = mutableStateOf(5)
    val countdownTime:State<Int> = _countdownTime

    private val _currentTime = mutableStateOf(0L)
    val currentTime: State<Long> = _currentTime

    private val listener = object : Player.Listener {
        override fun onEvents(
            player: Player,
            events: Player.Events
        ) {
            super.onEvents(player, events)
            _totalDuration.value = player.duration.coerceAtLeast(0L)
            _currentTime.value = player.currentPosition.coerceAtLeast(0L)
            _bufferedPercentage.value = player.bufferedPercentage
        }
    }

    fun seekTo(timeMs: Long) {
        exoPlayerInstance.seekTo(timeMs)
    }

    init {
        savedStateHandle.get<String>(VideoPlayerScreen.MovieIdBundleKey)?.let { movieId ->
            viewModelScope.launch {
                try {
                    val details = repository.getMovieDetails(movieId)
                    _uiState.value = VideoPlayerScreenUiState.Done(movieDetails = details)
                    if (!isPlayerPrepared) {
                        prepareExoPlayer(details)
                        isPlayerPrepared = true
                    }
                } catch (e: Exception) {
                    _uiState.value = VideoPlayerScreenUiState.Error
                }
            }
        } ?: run {
            _uiState.value = VideoPlayerScreenUiState.Error
        }

        viewModelScope.launch {
            while (true) {
                delay(300)
                _contentCurrentPosition.value = exoPlayer.currentPosition
                _isPlaying.value = exoPlayer.isPlaying
            }
        }
        repeatAdDisplay()
    }

    private fun repeatAdDisplay() {
        viewModelScope.launch {
            if (SplashActivity.showAd(context) == "yes") {
                repeat(50) {
                    delay(90000)
                    _adCountdownEnabled.value = true
                    _countdownTime.value = 5

                    delay(5000)
                    _adCountdownEnabled.value = false
                }
            }
        }
        if (adCountdownEnabled.value && countdownTime.value > 0) {
            viewModelScope.launch {
                repeat(countdownTime.value) {
                    delay(1000) // 1 second
                    _countdownTime.value -= 1
                }
            }
        }
    }

    private fun prepareExoPlayer(movieDetails: MovieDetails) {
        exoPlayer.setMediaItem(MediaItem.Builder()
            .setUri(movieDetails.videoUri)
            .build())

        exoPlayerInstance.addListener(listener)
        exoPlayer.seekTo(_contentCurrentPosition.value)
        exoPlayer.playWhenReady
        exoPlayer.prepare()
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayerInstance.removeListener(listener)
        exoPlayer.release()
    }
}

@Immutable
sealed class VideoPlayerScreenUiState {
    data object Loading : VideoPlayerScreenUiState()
    data object Error : VideoPlayerScreenUiState()
    data class Done(val movieDetails: MovieDetails) : VideoPlayerScreenUiState()
}