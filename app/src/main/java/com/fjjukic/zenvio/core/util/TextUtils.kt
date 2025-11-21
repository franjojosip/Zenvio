package com.fjjukic.zenvio.core.util

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