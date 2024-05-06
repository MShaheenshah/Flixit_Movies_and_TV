package com.programmerpro.cricketlivestreaming.presentation.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmerpro.cricketlivestreaming.data.entities.MovieList
import com.programmerpro.cricketlivestreaming.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MoviesScreenViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    val uiState = combine(
        movieRepository.getMoviesWithLongThumbnail(),
        movieRepository.getPopularFilmsThisWeek(),
    ) { (movieList, popularFilmsThisWeek) ->
        MoviesScreenUiState.Ready(movieList = movieList, popularFilmsThisWeek = popularFilmsThisWeek)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MoviesScreenUiState.Loading
    )

}

sealed interface MoviesScreenUiState {
    data object Loading: MoviesScreenUiState
    data class Ready(val movieList: MovieList, val popularFilmsThisWeek: MovieList): MoviesScreenUiState
}