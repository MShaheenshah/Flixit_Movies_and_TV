package com.programmerpro.cricketlivestreaming.presentation.screens.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.programmerpro.cricketlivestreaming.presentation.utils.createInitialFocusRestorerModifiers

@Composable
fun MovieFilterChipRow(
    filterList: FilterList,
    selectedFilterList: FilterList,
    onSelectedFilterListUpdated: (FilterList) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRestorerModifiers = createInitialFocusRestorerModifiers()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .then(focusRestorerModifiers.parentModifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val chunkedList = filterList.items.chunked(2) // Split the list into chunks of 2 items
        chunkedList.forEach { chunk ->
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    chunk.forEach { filterCondition ->
                        val isChecked = selectedFilterList.items.contains(filterCondition)
                        MovieFilterChip(
                            label = stringResource(id = filterCondition.labelId),
                            isChecked = isChecked,
                            onCheckedChange = {
                                val updated = if (it) {
                                    selectedFilterList.items + listOf(filterCondition)
                                } else {
                                    selectedFilterList.items - setOf(filterCondition)
                                }
                                onSelectedFilterListUpdated(FilterList(updated))
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}