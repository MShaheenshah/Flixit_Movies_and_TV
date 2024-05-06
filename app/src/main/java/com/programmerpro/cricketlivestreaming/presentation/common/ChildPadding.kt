package com.programmerpro.cricketlivestreaming.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.programmerpro.cricketlivestreaming.presentation.utils.Padding

val ParentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)

@Composable
fun rememberChildPadding(direction: LayoutDirection = LocalLayoutDirection.current): Padding {
    return remember {
        Padding(
            start = ParentPadding.calculateStartPadding(direction) + 8.dp,
            top = ParentPadding.calculateTopPadding(),
            end = ParentPadding.calculateEndPadding(direction) + 8.dp,
            bottom = ParentPadding.calculateBottomPadding()
        )
    }
}