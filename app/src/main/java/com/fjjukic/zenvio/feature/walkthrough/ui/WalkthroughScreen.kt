package com.fjjukic.zenvio.feature.walkthrough.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.core.util.CustomSystemBars
import com.fjjukic.zenvio.core.util.findActivity
import com.fjjukic.zenvio.feature.walkthrough.WalkthroughViewModel
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughEffect
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughIntent
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughPage
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughUiState
import com.fjjukic.zenvio.ui.theme.DividerLight
import com.fjjukic.zenvio.ui.theme.ZenvioTheme
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun WalkthroughScreen(
    viewModel: WalkthroughViewModel = hiltViewModel(),
    onFinished: () -> Unit
) {
    CustomSystemBars(lightStatusBarIcons = false, lightNavigationBarIcons = true)

    val activity = LocalContext.current.findActivity()
    val uiState by viewModel.uiState.collectAsState()

    // Create the PagerState here so it persists across recompositions from the ViewModel
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { uiState.pages.size }
    )

    // Collect one-time effects from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WalkthroughEffect.WalkthroughCanceled -> activity?.finish()
                is WalkthroughEffect.WalkthroughFinished -> onFinished()
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                viewModel.onIntent(WalkthroughIntent.PageChanged(page))
            }
    }

    LaunchedEffect(uiState.currentPage) {
        if (!pagerState.isScrollInProgress &&
            uiState.currentPage != pagerState.currentPage
        ) {
            pagerState.animateScrollToPage(uiState.currentPage)
        }
    }

    WalkthroughScreenStateless(
        uiState = uiState,
        pagerState = pagerState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun WalkthroughScreenStateless(
    uiState: WalkthroughUiState,
    pagerState: PagerState,
    onIntent: (WalkthroughIntent) -> Unit
) {
    if (uiState.pages.isEmpty()) {
        return
    }

    BackHandler(enabled = uiState.currentPage > 0) {
        onIntent(WalkthroughIntent.BackPressed)
    }

    Scaffold(
        bottomBar = {
            WalkthroughBottomBar(
                modifier = Modifier.navigationBarsPadding(),
                pagerState = pagerState,
                uiState = uiState,
                onStartBtnClick = { onIntent(WalkthroughIntent.StartButtonClick) },
                onEndBtnClick = { onIntent(WalkthroughIntent.EndButtonClick) }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            key = { index -> uiState.pages[index].hashCode() },
            userScrollEnabled = true,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) { position ->
            WalkthroughPageScreen(uiState.pages[position])
        }
    }
}

@Composable
fun WalkthroughBottomBar(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    uiState: WalkthroughUiState,
    onStartBtnClick: () -> Unit = {},
    onEndBtnClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StretchIndicator(
            modifier = Modifier.padding(top = 24.dp),
            pagerState = pagerState,
            count = uiState.pages.size,
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 24.dp),
            thickness = 1.dp,
            color = DividerLight
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Re-arranged the weight modifier to be more robust
            val startBtnText = uiState.startBtnText
            if (startBtnText != null) {
                Button(
                    onClick = onStartBtnClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) { Text(stringResource(startBtnText)) }
            }

            Button(
                onClick = onEndBtnClick,
                modifier = Modifier
                    .weight(if (startBtnText == null) 2f else 1f) // Take full width if it's the only button
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) { Text(stringResource(uiState.endBtnText)) }
        }
    }
}

@Composable
fun StretchIndicator(
    pagerState: PagerState,
    count: Int,
    modifier: Modifier = Modifier,
    width: Dp = 8.dp,
    height: Dp = 8.dp,
    activeLineWidth: Dp = 24.dp,
    spacing: Dp = 8.dp,
) {
    val indicatorColor = MaterialTheme.colorScheme.primary

    Canvas(modifier) {
        val dotW = width.toPx()
        val dotH = height.toPx()
        val activeW = activeLineWidth.toPx()
        val space = spacing.toPx()
        val dotCount = count + 1

        val totalWidth = dotCount * dotW + (dotCount - 1) * space

        val startX = (size.width - totalWidth) / 2f

        val pageWithOffset = pagerState.currentPage + pagerState.currentPageOffsetFraction

        for (i in 0 until dotCount) {

            val distance = kotlin.math.abs(pageWithOffset - i)
            val factor = (1f - distance).coerceIn(0f, 1f)

            val w = dotW + (activeW - dotW) * factor

            val x = startX + i * (dotW + space)

            drawRoundRect(
                color = indicatorColor,
                topLeft = Offset(x, center.y - dotH / 2f),
                size = Size(w, dotH),
                cornerRadius = CornerRadius(50f)
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun WalkthroughContentPreview() {
    ZenvioTheme {
        val previewState = WalkthroughUiState(
            pages = listOf(
                WalkthroughPage(
                    imageRes = R.drawable.walkthrough_step_one,
                    titleRes = R.string.title_walkthrough_step_one,
                    descriptionRes = R.string.description_walkthrough_step_one
                ),
                WalkthroughPage(
                    imageRes = R.drawable.walkthrough_step_two,
                    titleRes = R.string.title_walkthrough_step_two,
                    descriptionRes = R.string.description_walkthrough_step_two
                ),
                WalkthroughPage(
                    imageRes = R.drawable.walkthrough_step_three,
                    titleRes = R.string.title_walkthrough_step_three,
                    descriptionRes = R.string.description_walkthrough_step_three
                )
            ),
            currentPage = 0
        )
        WalkthroughScreenStateless(
            uiState = previewState,
            pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { previewState.pages.size }),
            onIntent = {})
    }
}