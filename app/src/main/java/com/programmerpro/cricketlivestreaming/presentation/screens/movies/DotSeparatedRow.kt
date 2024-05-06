package com.programmerpro.cricketlivestreaming.presentation.screens.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun DotSeparatedRow(
    modifier: Modifier = Modifier,
    texts: List<String>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        texts.forEach { text ->
            Row {
                Box(
                    modifier = Modifier
                        .padding(8.dp, 6.dp, 8.dp, 0.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 1f))
                        .size(4.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        val colonIndex = text.indexOf(":")
                        if (colonIndex != -1) {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(text.substring(0, colonIndex + 1))
                            }
                            append(text.substring(colonIndex + 1))
                        } else {
                            append(text)
                        }
                    },
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}
