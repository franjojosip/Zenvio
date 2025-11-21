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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.core.util.getAlphaBasedOnDistance
import com.fjjukic.zenvio.core.util.getScaleBasedOnDistance
import com.fjjukic.zenvio.ui.theme.ZenvioTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun AgeStep(
    selectedAge: Int,
    visibleItemCount: Int,
    itemHeight: Dp,
    onAgeSelected: (Int) -> Unit
) {
    val ages = (18..100).toList()
    val startIndex = ages.indexOf(selectedAge).coerceIn(0, ages.lastIndex)

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)
    val flingBehavior = rememberSnapFlingBehavior(listState)

    val sideCount = (visibleItemCount - 1) / 2
    val spacerHeight = itemHeight * sideCount
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }

    // Detect centered index while scrolling
    val centerIndex by remember {
        derivedStateOf {
            val firstVisibleIndex = listState.firstVisibleItemIndex
            val firstVisibleOffset = listState.firstVisibleItemScrollOffset

            val centerItemIndex =
                firstVisibleIndex + sideCount + (firstVisibleOffset / itemHeightPx).roundToInt()

            // Subtract 1 because the LazyColumn has a spacer item at the top (index 0).
            centerItemIndex - 1
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { !it }
            .collect {
                onAgeSelected(ages[centerIndex])
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
                .offset(y = -(itemHeight / 2))
                .width(200.dp)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.primary)
        )

        // Bottom line
        Box(
            modifier = Modifier
                .offset(y = (itemHeight / 2))
                .width(200.dp)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.primary)
        )

        // Main wheel
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier
                .height(itemHeight * visibleItemCount)
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
                AgePickerItem(
                    age = ages[index],
                    isCentered = index == centerIndex,
                    distanceFromCenter = abs(index - centerIndex),
                    itemHeight = itemHeight
                )
            }

            // Bottom spacer
            item {
                Spacer(modifier = Modifier.height(spacerHeight))
            }
        }
    }
}

@Composable
private fun AgePickerItem(
    age: Int,
    isCentered: Boolean,
    distanceFromCenter: Int,
    itemHeight: Dp
) {
    val scale by animateFloatAsState(
        targetValue = getScaleBasedOnDistance(distanceFromCenter),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "picker-scale"
    )

    val color by animateColorAsState(
        targetValue = if (isCentered) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(
                alpha = getAlphaBasedOnDistance(
                    distanceFromCenter
                )
            )
        },
        animationSpec = tween(150),
        label = "wheel-color"
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
                    scaleX = scale
                    scaleY = scale
                },
            color = color,
            style = MaterialTheme.typography.displayMedium.copy(
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AgeStepPreview() {
    ZenvioTheme {
        AgeStep(selectedAge = 25, visibleItemCount = 7, itemHeight = 60.dp, onAgeSelected = {})
    }
}