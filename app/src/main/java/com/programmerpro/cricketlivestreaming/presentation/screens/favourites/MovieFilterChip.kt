package com.programmerpro.cricketlivestreaming.presentation.screens.favourites

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MovieFilterChip(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFocused by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .padding(end = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onCheckedChange(!isChecked) },
        border = if (isFocused) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = null, // Disable the default Checkbox behavior
                colors = CheckboxDefaults.colors(
                    checkmarkColor = MaterialTheme.colorScheme.onSurface,
                    checkedColor = MaterialTheme.colorScheme.primary,
                    disabledCheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0f)
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
            ProvideTextStyle(
                value = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}
