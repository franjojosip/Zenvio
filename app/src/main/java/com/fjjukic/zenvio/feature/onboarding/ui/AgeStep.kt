package com.fjjukic.zenvio.feature.onboarding.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.core.util.getAlphaBasedOnDistance
import com.fjjukic.zenvio.core.util.getScaleBasedOnDistance
import com.fjjukic.zenvio.ui.theme.ZenvioTheme
import kotlinx.coroutines.flow.filter
import kotlin.math.abs


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AgeStepPreview() {
    ZenvioTheme {
        AgeStep(15) {}
    }
}


@Composable
fun AgeStep(
    selectedAge: Int,
    onAgeSelected: (Int) -> Unit
) {
    val ages = (18..100).toList()
    val startIndex = ages.indexOf(selectedAge).coerceIn(0, ages.lastIndex)

    val listState = rememberLazyListState(startIndex + 1)
    val flingBehavior = rememberSnapFlingBehavior(listState)

    val itemHeight = 60.dp
    val visibleCount = 7 // center + 3 above + 3 below
    val sideCount = (visibleCount - 1) / 2

    val spacerHeight = itemHeight * sideCount

    // Detect centered index while scrolling
    val centerIndex by remember {
        derivedStateOf {
            val layout = listState.layoutInfo
            val centerY = layout.viewportSize.height / 2f

            val centeredLazyItem = layout.visibleItemsInfo.minByOrNull { item ->
                val itemCenter = item.offset + item.size / 2f
                abs(itemCenter - centerY)
            }

            if (centeredLazyItem == null) {
                startIndex
            } else {
                val lazyIndex = centeredLazyItem.index - 1
                lazyIndex.coerceIn(0, ages.lastIndex)
            }
        }
    }

    BaseStep(
        titleRes = R.string.title_onboarding_age_step,
        subtitleRes = R.string.subtitle_onboarding_age_step
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "years",
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 70.dp, y = 6.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )

            // Top line
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = -(itemHeight / 2))
                    .width(200.dp)
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )

            // Bottom line
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (itemHeight / 2))
                    .width(200.dp)
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )

            // MAIN WHEEL
            LazyColumn(
                state = listState,
                flingBehavior = flingBehavior,
                modifier = Modifier
                    .height(itemHeight * visibleCount)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Top spacer
                item {
                    Spacer(modifier = Modifier.height(spacerHeight))
                }

                // Age scroll items
                items(ages.size, key = { it }) { index ->
                    val age = ages[index]
                    val distance = abs(index - centerIndex)

                    // Smooth stepped scale
                    val scale = getScaleBasedOnDistance(distance)

                    // Color logic
                    val textColor = if (index == centerIndex) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Black.copy(alpha = getAlphaBasedOnDistance(distance))
                    }
                    val animatedColor by animateColorAsState(
                        targetValue = textColor,
                        animationSpec = tween(120),
                        label = "wheel-color"
                    )
                    val animatedScale by animateFloatAsState(
                        targetValue = scale,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        ),
                        label = "picker-scale"
                    )
                    Box(
                        modifier = Modifier
                            .height(itemHeight)
                            .fillMaxWidth()
                            .clipToBounds()
                    ) {
                        Text(
                            text = age.toString(),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .graphicsLayer {
                                    scaleX = animatedScale
                                    scaleY = animatedScale
                                    alpha = animatedScale
                                },
                            color = animatedColor,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 40.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }

                // Bottom spacer
                item {
                    Spacer(modifier = Modifier.height(spacerHeight))
                }
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it }
            .collect {
                onAgeSelected(ages[centerIndex])
            }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun NumberPreview() {
    ZenvioTheme {
        Number(25, 1f)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun Number2Preview() {
    ZenvioTheme {
        Number(25, 0.9f)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun Number223Preview() {
    ZenvioTheme {
        Number(25, 0.8f)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun Number23Preview() {
    ZenvioTheme {
        Number(25, 0.7f)
    }
}

@Composable
fun Number(value: Int, scale: Float) {
    Text(
        text = value.toString(),
        modifier = Modifier
            .padding(vertical = 24.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale

                alpha = 1f
            },
        style = MaterialTheme.typography.displayLarge.copy(
            fontSize = 40.sp,
            textAlign = TextAlign.Center
        )
    )
}