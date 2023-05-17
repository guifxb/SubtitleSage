package com.example.translatorsportfolio.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


@Composable
fun PortfolioTheme(
    windowSizeClass: WindowsSizeClass,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
) {
    val view = LocalView.current
    val colors = if (!useDarkTheme) LightColors else DarkColors

    val orientation = when {
        windowSizeClass.width.size > windowSizeClass.height.size -> Orientation.Landscape
        else -> Orientation.Portrait
    }

    val mainSize = when(orientation) {
        Orientation.Portrait -> windowSizeClass.width
        Orientation.Landscape -> windowSizeClass.height
    }

    val dimensions = when(mainSize) {
        is WindowSize.Small -> smallDimensions
        is WindowSize.Compact -> compactDimensions
        is WindowSize.Medium -> mediumDimensions
        else -> largeDimensions
    }

    val typography = when(mainSize) {
        is WindowSize.Small -> typographySmall
        is WindowSize.Compact -> typographyCompact
        is WindowSize.Medium -> typographyMedium
        else -> typographyLarge
    }

    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = colors.surfaceVariant.toArgb()
        window.navigationBarColor = colors.surfaceVariant.toArgb()
        WindowCompat.getInsetsController(window, view)
            .isAppearanceLightStatusBars = !useDarkTheme
        WindowCompat.getInsetsController(window, view)
            .isAppearanceLightNavigationBars = !useDarkTheme
    }


    ProvideAppUtils(dimensions = dimensions, orientation = orientation) {
        MaterialTheme(
            colorScheme = colors,
            content = content,
            typography = typography
        )
    }
}

object AppTheme {
    val dimens: Dimensions
    @Composable
    get() = LocalAppDimens.current

    val orient: Orientation
    @Composable
    get() = LocalOrientation.current

}
