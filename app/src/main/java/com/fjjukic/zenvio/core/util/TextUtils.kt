package com.fjjukic.zenvio.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fjjukic.zenvio.feature.chat.model.UiText

fun getAlphaBasedOnDistance(distance: Int): Float {
    return when (distance) {
        0 -> 1f
        1 -> 0.75f
        2 -> 0.7f
        else -> 0.65f
    }
}

fun getScaleBasedOnDistance(distance: Int): Float {
    return when (distance) {
        0 -> 1.3f
        1 -> 1f
        2 -> 0.8f
        else -> 0.6f
    }
}

@Composable
fun UiText.asString(): String {
    return when (this) {
        is UiText.Dynamic -> value
        is UiText.StringResource ->
            if (args.isNotEmpty()) {
                stringResource(resId, *args.toTypedArray())
            } else {
                stringResource(resId)
            }
    }
}