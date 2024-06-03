package com.boukhari.cats.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White

val LightGray = Color(0xFFF5F5F5)

@Composable
fun CATheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = Black,
        onPrimary = Black,
        secondary = White,
        onSecondary = Black,
        background = LightGray,
        onBackground = Black,
        surface = White,
        onSurface = Black,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

