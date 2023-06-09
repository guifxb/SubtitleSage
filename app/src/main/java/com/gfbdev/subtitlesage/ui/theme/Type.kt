package com.gfbdev.subtitlesage.ui.theme


import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gfbdev.subtitlesage.R


val Montserrat =
    FontFamily(Font(R.font.montserrat_regular), Font(R.font.montserrat_bold, FontWeight.Bold))

val typographySmall = Typography(
    headlineLarge = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 22.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 18.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 14.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Montserrat, fontSize = 12.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Montserrat, fontSize = 10.sp
    ),
)

val typographyCompact = Typography(
    headlineLarge = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 24.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 22.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Montserrat, fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Montserrat, fontSize = 10.sp
    ),
)

val typographyMedium = Typography(
    headlineLarge = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Montserrat, fontSize = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Montserrat, fontSize = 18.sp
    ),
)

val typographyLarge = Typography(
    headlineLarge = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 36.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 32.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Montserrat, fontWeight = FontWeight.Medium, fontSize = 28.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Montserrat, fontSize = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Montserrat, fontSize = 22.sp
    ),
)