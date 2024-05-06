package com.programmerpro.cricketlivestreaming.presentation.screens.home

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmerpro.cricketlivestreaming.data.entities.MovieList
import com.programmerpro.cricketlivestreaming.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeScreeViewModel @Inject constructor(
    movieRepository: MovieRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeScreenUiState> = combine(
        movieRepository.getFeaturedMovies(),
        movieRepository.getTrendingMovies(),
        movieRepository.getTop10Movies(),
        movieRepository.getNowPlayingMovies(),
    ) { featuredMovieList, trendingMovieList, top10MovieList, nowPlayingMovieList ->
        HomeScreenUiState.Ready(
            featuredMovieList,
            trendingMovieList,
            top10MovieList,
            nowPlayingMovieList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeScreenUiState.Loading
    )
}

sealed interface HomeScreenUiState {
    data object Loading: HomeScreenUiState
    data object Error: HomeScreenUiState
    data class Ready(
        val featuredMovieList: MovieList,
        val trendingMovieList: MovieList,
        val top10MovieList: MovieList,
        val nowPlayingMovieList: MovieList
    ): HomeScreenUiState
}