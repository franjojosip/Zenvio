package com.fjjukic.zenvio.ui.defaults

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppInputDefaults {

    val textFieldTextColor: Color
        @Composable get() = Color.Black.copy(alpha = 0.8f)

    val navigationItemColor: Color
        @Composable get() = Color.Black.copy(alpha = 0.25f)

    val itemColor: Color
        @Composable get() = Color.Black.copy(alpha = 0.85f)

    val hintColor: Color
        @Composable get() = Color.Black.copy(alpha = 0.35f)

    val cursorColor: Color
        @Composable get() = Color.Black.copy(alpha = 0.8f)

    val borderColor: Color
        @Composable get() = Color(0xFFCCCCCC)

    val boxFillColor: Color
        @Composable get() = Color(0xFFF5F5F5)
}