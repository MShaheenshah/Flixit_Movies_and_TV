package com.programmerpro.cricketlivestreaming.presentation.screens.categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmerpro.cricketlivestreaming.data.entities.MovieCategoryDetails
import com.programmerpro.cricketlivestreaming.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CategoryMovieListScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    movieRepository: MovieRepository
) : ViewModel() {

    val uiState =
        savedStateHandle.getStateFlow<String?>(
            CategoryMovieListScreen.CategoryIdBundleKey,
            null
        ).map { id ->
            if (id == null) {
                CategoryMovieListScreenUiState.Error
            } else {
                val categoryDetails = movieRepository.getMovieCategoryDetails(id)
                CategoryMovieListScreenUiState.Done(categoryDetails)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CategoryMovieListScreenUiState.Loading
        )
}

sealed interface CategoryMovieListScreenUiState {
    data object Loading : CategoryMovieListScreenUiState
    data object Error : CategoryMovieListScreenUiState
    data class Done(val movieCategoryDetails: MovieCategoryDetails) : CategoryMovieListScreenUiState
}