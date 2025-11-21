package com.fjjukic.zenvio.feature.preparing_plan.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.core.util.CustomSystemBars
import com.fjjukic.zenvio.feature.preparing_plan.PreparingPlanViewModel
import com.fjjukic.zenvio.feature.preparing_plan.model.PreparingPlanIntent
import com.fjjukic.zenvio.ui.theme.ZenvioTheme

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreparingPlanScreenPreview() {
    ZenvioTheme {
        PreparingPlanScreen()
    }
}

@Composable
fun PreparingPlanScreen(
    viewModel: PreparingPlanViewModel = hiltViewModel(),
    onFinished: () -> Unit = {}
) {
    CustomSystemBars(lightStatusBarIcons = true, lightNavigationBarIcons = true)
    val state = viewModel.uiState

    BackHandler {
        // ignore back press
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.title_preparing_plan),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.85f)
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.label_please_wait),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray
                )
            )
        }

        CircularPercentageIndicator(
            progress = state.progress,
            strokeWidth = 20.dp
        )

        Text(
            text = stringResource(R.string.label_this_will_take_a_moment),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.Gray,
                lineHeight = 26.sp
            ),
            modifier = Modifier.padding(bottom = 40.dp)
        )
    }

    // Start loading when entering screen
    LaunchedEffect(Unit) {
        viewModel.onIntent(PreparingPlanIntent.Start)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PreparingPlanViewModel.PreparingPlanEffect.OnFinished -> {
                    onFinished()
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CircularPercentageIndicatorPreview() {
    ZenvioTheme {
        CircularPercentageIndicator(75)
    }
}

@Composable
fun CircularPercentageIndicator(
    progress: Int,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 16.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(240.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            // Background arc
            drawArc(
                color = Color(0xFFDFDFDF),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            // Foreground arc
            drawArc(
                color = Color(0xFF8DAA57), // green
                startAngle = -90f,
                sweepAngle = (progress / 100f) * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Text(
            text = "${progress}%",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}