package com.programmerpro.cricketlivestreaming.presentation.screens.shows

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
class ShowScreenViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    val uiState = combine(
        movieRepository.getBingeWatchDramas(),
        movieRepository.getTVShows()
    ) { (bingeWatchDramaList, tvShowList) ->
        ShowScreenUiState.Ready(bingeWatchDramaList = bingeWatchDramaList, tvShowList = tvShowList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShowScreenUiState.Loading
    )

}

sealed interface ShowScreenUiState {
    data object Loading : ShowScreenUiState
    data class Ready(val bingeWatchDramaList: MovieList, val tvShowList: MovieList): ShowScreenUiState

}