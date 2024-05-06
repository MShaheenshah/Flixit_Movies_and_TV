package com.programmerpro.cricketlivestreaming.presentation.screens.categories

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Handler
import android.view.OrientationEventListener
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.programmerpro.cricketlivestreaming.data.entities.MovieCategoryList
import com.programmerpro.cricketlivestreaming.must.ads.Admob_interstitial
import com.programmerpro.cricketlivestreaming.presentation.common.rememberChildPadding
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamBorderWidth
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamCardShape
import com.programmerpro.cricketlivestreaming.presentation.utils.GradientBg

@Composable
fun CategoriesScreen(
    gridColumns: Int = 3,
    onCategoryClick: (categoryId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    categoriesScreenViewModel: CategoriesScreenViewModel = hiltViewModel()
) {

    val uiState by categoriesScreenViewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        is CategoriesScreenUiState.Loading -> {
            Loading()
        }
        is CategoriesScreenUiState.Ready -> {
            Catalog(
                gridColumns = gridColumns,
                movieCategories = s.categoryList,
                onCategoryClick = onCategoryClick,
                onScroll = onScroll,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Catalog(
    movieCategories: MovieCategoryList,
    modifier: Modifier = Modifier,
    gridColumns: Int = 3,
    onCategoryClick: (categoryId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
) {
    val childPadding = rememberChildPadding()
    val tvLazyGridState = rememberLazyGridState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            tvLazyGridState.firstVisibleItemIndex == 0 &&
                    tvLazyGridState.firstVisibleItemScrollOffset < 100
        }
    }
    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }

    val activity = LocalContext.current as Activity
    val currentOrientation = activity.resources.configuration.orientation

    Admob_interstitial.checkingCounter(activity)

    DisposableEffect(Unit) {
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
            orientationEventListener.disable()
        }
    }

    AnimatedContent(
        targetState = movieCategories,
        modifier = Modifier
            .padding(horizontal = childPadding.start)
            .padding(top = childPadding.top),
        label = "",
    ) { it ->
        LazyVerticalGrid(
            state = tvLazyGridState,
            modifier = modifier,
            columns = GridCells.Fixed(gridColumns),
            userScrollEnabled = true,
            content = {
                itemsIndexed(it) { index, movieCategory ->
                    var isFocused by remember { mutableStateOf(false) }
                    Card(
                        shape = JetStreamCardShape,
                        modifier = modifier
                            .border(
                                border = BorderStroke(
                                    width = JetStreamBorderWidth,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                            )
                            .scale(1f)
                            .clickable { onCategoryClick(movieCategory.id) }
                            .padding(8.dp)
                            .aspectRatio(16 / 9f)
                            .onFocusChanged {
                                isFocused = it.isFocused || it.hasFocus
                            }
                            .focusProperties {
                                if (index % gridColumns == 0) {
                                    left = FocusRequester.Cancel
                                }
                            },
                    ){
                        val itemAlpha by animateFloatAsState(
                            targetValue = if (isFocused) .6f else 0.2f,
                            label = ""
                        )
                        val textColor = if (isFocused) Color.White else Color.White

                        Box(contentAlignment = Alignment.Center) {
                            Box(modifier = Modifier.alpha(itemAlpha)) {
                                GradientBg()
                            }
                            Text(
                                text = movieCategory.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = textColor,
                                )
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Text(text = "Loading...", modifier = modifier)
}