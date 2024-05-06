package com.programmerpro.cricketlivestreaming.presentation.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmerpro.cricketlivestreaming.data.entities.MovieCategoryList
import com.programmerpro.cricketlivestreaming.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CategoriesScreenViewModel @Inject constructor(
    movieRepository: MovieRepository
): ViewModel() {

    val uiState = movieRepository.getMovieCategories().map {
        CategoriesScreenUiState.Ready(categoryList = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CategoriesScreenUiState.Loading
    )

}

sealed interface CategoriesScreenUiState {

    data object Loading: CategoriesScreenUiState
    data class Ready(val categoryList: MovieCategoryList): CategoriesScreenUiState

}