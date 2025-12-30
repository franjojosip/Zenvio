package com.fjjukic.zenvio.feature.breathing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.feature.breathing.model.BreathingIntent
import com.fjjukic.zenvio.feature.breathing.model.BreathingPhase
import com.fjjukic.zenvio.feature.breathing.model.BreathingUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class BreathingViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(BreathingUiState())
    val state = _state.asStateFlow()

    init {
        startBreathingLoop()
    }

    private fun startBreathingLoop() {
        viewModelScope.launch {
            while (true) {
                if (!_state.value.isPaused) {
                    runBreathingCycle()
                } else {
                    delay(200)
                }
            }
        }
    }

    private suspend fun runBreathingCycle() {
        val mode = _state.value.currentMode

        // Inhale
        runPhase(BreathingPhase.Inhale, mode.inhale)

        // Hold
        if (mode.hold > 0) {
            runPhase(BreathingPhase.Hold, mode.hold)
        }

        // Exhale
        runPhase(BreathingPhase.Exhale, mode.exhale)

        // End of cycle
        _state.update { it.copy(cycleCount = it.cycleCount + 1) }
    }

    private suspend fun runPhase(phase: BreathingPhase, seconds: Int) {
        _state.update {
            it.copy(
                phase = phase,
                remainingSeconds = seconds
            )
        }

        for (sec in seconds downTo 1) {
            if (_state.value.isPaused) break
            _state.update { it.copy(remainingSeconds = sec) }
            delay(1000)
        }
    }

    fun onIntent(intent: BreathingIntent) {
        when (intent) {

            is BreathingIntent.ChangeMode -> {
                _state.update {
                    BreathingUiState(currentMode = intent.mode) // reset state
                }
            }

            BreathingIntent.Restart -> {
                _state.update { BreathingUiState(currentMode = it.currentMode) }
            }

            BreathingIntent.TogglePause -> {
                _state.update { it.copy(isPaused = !it.isPaused) }
            }

            BreathingIntent.Back -> { /* handled by UI */
            }
        }
    }
}
