package com.fjjukic.zenvio.feature.preparing_plan.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.fjjukic.zenvio.feature.preparing_plan.model.PreparingPlanEffect
import com.fjjukic.zenvio.feature.preparing_plan.model.PreparingPlanIntent
import com.fjjukic.zenvio.ui.theme.ZenvioTheme

@Composable
fun PreparingPlanScreen(
    viewModel: PreparingPlanViewModel = hiltViewModel(),
    onNavigateToNextScreen: () -> Unit = {}
) {
    CustomSystemBars(lightStatusBarIcons = true, lightNavigationBarIcons = true)

    var progressTarget by remember { mutableIntStateOf(0) }

    LaunchedEffect(viewModel) {
        viewModel.onIntent(PreparingPlanIntent.Start)
        progressTarget = 100

        viewModel.effect.collect { effect ->
            when (effect) {
                PreparingPlanEffect.OnFinished -> onNavigateToNextScreen()
            }
        }
    }

    BackHandler {}

    PreparingPlanScreenStateless(
        progress = progressTarget,
        animationDuration = PreparingPlanViewModel.TOTAL_PREPARATION_TIME_MS.toInt()
    )
}

@Composable
fun PreparingPlanScreenStateless(progress: Int, animationDuration: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.weight(0.4f))

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
        Spacer(modifier = Modifier.weight(0.5f))

        CircularPercentageIndicator(
            progress = progress,
            animationDuration = animationDuration,
            strokeWidth = 20.dp,
        )
        Spacer(modifier = Modifier.weight(0.5f))

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
}

@Composable
fun CircularPercentageIndicator(
    progress: Int,
    animationDuration: Int,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 16.dp
) {
    val animationSpec = tween<Float>(
        durationMillis = animationDuration,
        easing = LinearEasing
    )
    val intAnimationSpec = tween<Int>(
        durationMillis = animationDuration,
        easing = LinearEasing
    )

    val animatedSweepAngle by animateFloatAsState(
        targetValue = (progress / 100f) * 360f,
        animationSpec = animationSpec,
        label = "ProgressSweepAnimation"
    )

    val animatedProgressText by animateIntAsState(
        targetValue = progress,
        animationSpec = intAnimationSpec,
        label = "ProgressTextAnimation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(240.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background arc
            drawArc(
                color = Color(0xFFF0F0F0),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            // Foreground arc (animated)
            drawArc(
                color = Color(0xFF8DAA57), // green
                startAngle = -90f,
                sweepAngle = animatedSweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Text(
            text = "$animatedProgressText%",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.8f)
            )
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreparingPlanScreenPreview() {
    ZenvioTheme {
        PreparingPlanScreenStateless(progress = 75, animationDuration = 2000)
    }
}