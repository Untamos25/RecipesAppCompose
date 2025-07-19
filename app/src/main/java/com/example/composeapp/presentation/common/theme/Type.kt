package com.example.composeapp.presentation.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val recipesAppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = montserratAlternatesFontFamily,
        fontWeight = W600,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = montserratAlternatesFontFamily,
        fontWeight = W600,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = montserratFontFamily,
        fontWeight = W400,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = montserratFontFamily,
        fontWeight = W500,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = montserratFontFamily,
        fontWeight = W600,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = montserratFontFamily,
        fontWeight = W400,
        fontSize = 16.sp
    ),
)