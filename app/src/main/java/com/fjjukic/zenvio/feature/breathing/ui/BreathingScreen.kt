package com.fjjukic.zenvio.feature.breathing.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fjjukic.zenvio.feature.breathing.BreathingViewModel
import com.fjjukic.zenvio.feature.breathing.model.BreathingIntent
import com.fjjukic.zenvio.feature.breathing.model.BreathingMode
import com.fjjukic.zenvio.feature.breathing.model.BreathingPhase
import com.fjjukic.zenvio.feature.breathing.model.BreathingUiState

@Composable
fun BreathingScreen(
    onBack: () -> Unit,
    viewModel: BreathingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    BreathingScreenStateless(
        state = state,
        onIntent = viewModel::onIntent,
        onBack = onBack
    )
}

@Composable
fun BreathingScreenStateless(
    state: BreathingUiState,
    onIntent: (BreathingIntent) -> Unit,
    onBack: () -> Unit
) {
    val animation = rememberInfiniteTransition()

    val scale by animation.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = state.remainingSeconds * 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing-scale"
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Breathing — ${state.currentMode.name}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(40.dp))

        // Animated Circle
        Box(
            Modifier
                .size(240.dp)
                .scale(scale)
                .background(
                    brush = Brush.radialGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {}

        Spacer(Modifier.height(24.dp))

        Text(
            text = when (state.phase) {
                BreathingPhase.Inhale -> "Inhale"
                BreathingPhase.Hold -> "Hold"
                BreathingPhase.Exhale -> "Exhale"
            },
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "${state.remainingSeconds}s",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        )

        Spacer(Modifier.height(24.dp))

        // Mode Selector
        BreathingModeSelector(
            current = state.currentMode,
            onSelect = { onIntent(BreathingIntent.ChangeMode(it)) }
        )

        Spacer(Modifier.height(16.dp))

        // Pause/Play
        IconButton(onClick = { onIntent(BreathingIntent.TogglePause) }) {
            Icon(
                if (state.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text("Cycles completed: ${state.cycleCount}")
    }
}


@Composable
fun BreathingModeSelector(
    current: BreathingMode,
    onSelect: (BreathingMode) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Breathing Mode", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        val modes = listOf(
            BreathingMode.Calm,
            BreathingMode.Box,
            BreathingMode.FourSevenEight
        )

        modes.forEach { mode ->
            FilterChip(
                selected = mode == current,
                onClick = { onSelect(mode) },
                label = { Text(mode.name) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
