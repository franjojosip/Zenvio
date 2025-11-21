package com.fjjukic.zenvio.feature.onboarding.ui.step

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
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

private const val VISIBLE_ITEM_COUNT = 7
private val ITEM_HEIGHT = 60.dp

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AgeStepPreview() {
    ZenvioTheme {
        AgeStep(selectedAge = 25, onAgeSelected = {})
    }
}


@Composable
fun AgeStep(
    selectedAge: Int,
    onAgeSelected: (Int) -> Unit
) {
    val ages = (18..100).toList()
    val startIndex = ages.indexOf(selectedAge).coerceIn(0, ages.lastIndex)

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)
    val flingBehavior = rememberSnapFlingBehavior(listState)

    val sideCount = (VISIBLE_ITEM_COUNT - 1) / 2
    val spacerHeight = ITEM_HEIGHT * sideCount

    // Detect centered index while scrolling
    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) {
                startIndex
            } else {
                val viewportCenter =
                    layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2
                visibleItemsInfo.minByOrNull {
                    abs((it.offset + it.size / 2) - viewportCenter)
                }?.index ?: startIndex
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it }
            .collect {
                val validIndex = centerIndex.coerceIn(ages.indices)
                onAgeSelected(ages[validIndex])
            }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.label_years),
            modifier = Modifier
                .offset(x = 70.dp, y = 6.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )

        // Top line
        Box(
            modifier = Modifier
                .offset(y = -(ITEM_HEIGHT / 2))
                .width(200.dp)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.primary)
        )

        // Bottom line
        Box(
            modifier = Modifier
                .offset(y = (ITEM_HEIGHT / 2))
                .width(200.dp)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.primary)
        )

        // Main wheel
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier
                .height(ITEM_HEIGHT * VISIBLE_ITEM_COUNT)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Top spacer
            item { Spacer(modifier = Modifier.height(spacerHeight)) }

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
                        .height(ITEM_HEIGHT)
                        .fillMaxWidth()
                        .clipToBounds(),
                    contentAlignment = Alignment.Center
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
            item { Spacer(modifier = Modifier.height(spacerHeight)) }
        }
    }
}