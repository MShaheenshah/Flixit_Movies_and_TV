package com.programmerpro.cricketlivestreaming.presentation.screens.movies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmerpro.cricketlivestreaming.data.entities.MovieDetails
import com.programmerpro.cricketlivestreaming.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MovieDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: MovieRepository,
) : ViewModel() {
    val uiState = savedStateHandle
        .getStateFlow<String?>(MovieDetailsScreen.MovieIdBundleKey, null)
        .map { id ->
            if (id == null) {
                MovieDetailsScreenUiState.Error
            } else {
                val details = repository.getMovieDetails(movieId = id)
                MovieDetailsScreenUiState.Done(movieDetails = details)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MovieDetailsScreenUiState.Loading
        )
}

sealed class MovieDetailsScreenUiState {
    data object Loading : MovieDetailsScreenUiState()
    data object Error : MovieDetailsScreenUiState()
    data class Done(val movieDetails: MovieDetails) : MovieDetailsScreenUiState()
}