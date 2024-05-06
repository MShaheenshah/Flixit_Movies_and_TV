package com.programmerpro.cricketlivestreaming.presentation.screens.movies

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TitleValueText(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .alpha(0.75f),
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
            maxLines = 1
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
            maxLines = 3
        )
    }
}
