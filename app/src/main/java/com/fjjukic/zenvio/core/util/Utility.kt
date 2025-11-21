package com.fjjukic.zenvio.core.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun CustomSystemBars(
    lightStatusBarIcons: Boolean = true,
    lightNavigationBarIcons: Boolean = true
) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val controller = WindowCompat.getInsetsController(window, view)

    DisposableEffect(Unit) {
        controller.isAppearanceLightStatusBars = lightStatusBarIcons
        controller.isAppearanceLightNavigationBars = lightNavigationBarIcons

        onDispose {}
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}