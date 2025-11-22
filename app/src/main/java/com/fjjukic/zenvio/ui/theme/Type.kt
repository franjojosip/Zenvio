package com.fjjukic.zenvio.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.fjjukic.zenvio.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val inter = FontFamily(
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
        weight = FontWeight.Light
    ),
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
        weight = FontWeight.Normal
    ),
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
        weight = FontWeight.Medium
    ),
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
        weight = FontWeight.SemiBold
    ),
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
        weight = FontWeight.Bold
    )
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 22.sp
    ),
    displayMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 34.sp,
        lineHeight = 22.sp
    ),
    displaySmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 22.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 22.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 22.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 22.sp
    ),
    titleLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 22.sp
    ),
    titleSmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    bodySmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 22.sp
    ),
    labelLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 22.sp
    ),
    labelMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.sp
    ),
    labelSmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 22.sp
    )
)
