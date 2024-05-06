package com.programmerpro.cricketlivestreaming.presentation.utils

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onPlaced

fun Modifier.occupyScreenSize() = this
    .fillMaxSize()
    .wrapContentSize()

@Composable
fun Modifier.focusOnInitialVisibility(isVisible: MutableState<Boolean>): Modifier {
    val focusRequester = remember { FocusRequester() }

    return focusRequester(focusRequester)
        .onPlaced {
            if (!isVisible.value) {
                focusRequester.requestFocus()
                isVisible.value = true
            }
        }
}

data class FocusRequesterModifiers(
    val parentModifier: Modifier,
    val childModifier: Modifier
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun createInitialFocusRestorerModifiers(): FocusRequesterModifiers {
    val focusRequester = remember { FocusRequester() }
    val childFocusRequester = remember { FocusRequester() }

    val parentModifier = Modifier
        .focusRequester(focusRequester)
        .focusProperties {
            exit = {
                focusRequester.freeFocus()
                FocusRequester.Default
            }
            enter = {
                if (focusRequester.captureFocus()) FocusRequester.Cancel
                else childFocusRequester
            }
        }

    val childModifier = Modifier.focusRequester(childFocusRequester)

    return FocusRequesterModifiers(parentModifier, childModifier)
}

fun Modifier.ifElse(
    condition: () -> Boolean,
    ifTrueModifier: Modifier,
    ifFalseModifier: Modifier = Modifier
): Modifier = then(if (condition()) ifTrueModifier else ifFalseModifier)

fun Modifier.ifElse(
    condition: Boolean,
    ifTrueModifier: Modifier,
    ifFalseModifier: Modifier = Modifier
): Modifier = ifElse({ condition }, ifTrueModifier, ifFalseModifier)