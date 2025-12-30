package com.fjjukic.zenvio.feature.breathing.model

data class BreathingUiState(
    val currentMode: BreathingMode = BreathingMode.Calm,
    val phase: BreathingPhase = BreathingPhase.Inhale,
    val remainingSeconds: Int = currentMode.inhale,
    val cycleCount: Int = 0,
    val isPaused: Boolean = false
)

enum class BreathingPhase {
    Inhale, Hold, Exhale
}

sealed interface BreathingIntent {
    data class ChangeMode(val mode: BreathingMode) : BreathingIntent
    data object Restart : BreathingIntent
    data object TogglePause : BreathingIntent
    data object Back : BreathingIntent
}

sealed class BreathingMode(
    val name: String,
    val inhale: Int,
    val hold: Int = 0,
    val exhale: Int
) {
    data object Calm : BreathingMode("Calm Breathing (4–6)", inhale = 4, exhale = 6)
    data object Box : BreathingMode("Box Breathing (4–4–4–4)", inhale = 4, hold = 4, exhale = 4)
    data object FourSevenEight : BreathingMode("4–7–8 Technique", inhale = 4, hold = 7, exhale = 8)

    data class Custom(
        val customName: String,
        val inhaleSec: Int,
        val holdSec: Int,
        val exhaleSec: Int
    ) : BreathingMode(customName, inhaleSec, holdSec, exhaleSec)
}