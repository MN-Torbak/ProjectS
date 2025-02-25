package com.example.projects.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.projects.R

val Comfortaa = FontFamily(
    Font(R.font.comfortaa_bold, FontWeight.Black, FontStyle.Normal),

    Font(R.font.comfortaa_semi_bold, FontWeight.Black, FontStyle.Normal),

    Font(R.font.comfortaa_light, FontWeight.Black, FontStyle.Normal),

    Font(R.font.comfortaa_medium, FontWeight.Black, FontStyle.Normal),

    Font(R.font.comfortaa_regular, FontWeight.Black, FontStyle.Normal)
)



// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Comfortaa,
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