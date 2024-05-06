package com.programmerpro.cricketlivestreaming.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.programmerpro.cricketlivestreaming.data.util.StringConstants
import com.programmerpro.cricketlivestreaming.presentation.screens.Screens
import com.programmerpro.cricketlivestreaming.presentation.theme.IconSize
import com.programmerpro.cricketlivestreaming.presentation.theme.JetStreamCardShape
import com.programmerpro.cricketlivestreaming.presentation.utils.occupyScreenSize

val TopBarTabs = Screens.entries.filter { it.isTabItem }

val TopBarFocusRequesters = List(size = TopBarTabs.size + 1) { FocusRequester() }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DashboardTopBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    screens: List<Screens> = TopBarTabs,
    focusRequesters: List<FocusRequester> = remember { TopBarFocusRequesters },
    onScreenSelection: (screen: Screens) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .focusRestorer(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                var isTabRowFocused by remember { mutableStateOf(false) }

                Spacer(modifier = Modifier.width(20.dp))
                TabRow(
                    modifier = Modifier
                        .onFocusChanged {
                            isTabRowFocused = it.isFocused || it.hasFocus
                        },
                    selectedTabIndex = selectedTabIndex,
                    indicator = { tabPositions ->
                        if (selectedTabIndex >= 0) {
                            DashboardTopBarItemIndicator(
                                currentTabPosition = tabPositions[selectedTabIndex],
                                anyTabFocused = isTabRowFocused,
                                shape = JetStreamCardShape
                            )
                        }
                    },
                    divider = { Spacer(modifier = Modifier.width(8.dp)) }
                ) {
                    screens.forEachIndexed { index, screen ->
                        key(index) {
                            val selected = index == selectedTabIndex
                            Tab(
                                modifier = Modifier
                                    .height(32.dp)
                                    .focusRequester(focusRequesters[index + 1]),
                                selected = selected,
                                onClick = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                    onScreenSelection(screen)
                                          },
                            ) {
                                if (screen.tabIcon != null) {
                                    Icon(
                                        screen.tabIcon,
                                        modifier = Modifier.padding(4.dp),
                                        contentDescription = StringConstants.Composable
                                            .ContentDescription.DashboardSearchButton,
                                        tint = LocalContentColor.current
                                    )
                                } else {
                                    Text(
                                        modifier = Modifier
                                            .occupyScreenSize()
                                            .padding(horizontal = 4.dp),
                                        text = screen(),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            color = LocalContentColor.current
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .alpha(0.75f)
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.PlayCircle,
                    contentDescription = StringConstants.Composable
                        .ContentDescription.BrandLogoImage,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(IconSize)
                )
            }
        }
    }
}
