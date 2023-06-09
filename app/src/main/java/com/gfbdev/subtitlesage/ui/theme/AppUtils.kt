package com.gfbdev.subtitlesage.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

@Composable
fun ProvideAppUtils(
    dimensions: Dimensions,
    orientation: Orientation,
    content: @Composable () -> Unit,
) {
    val dimensionSet = remember { dimensions }
    val orientation = remember { orientation }
    CompositionLocalProvider(
        LocalAppDimens provides dimensionSet,
        LocalOrientation provides orientation,
        content = content
    )
}

val LocalAppDimens = compositionLocalOf { smallDimensions }
val LocalOrientation = compositionLocalOf { Orientation.Portrait }