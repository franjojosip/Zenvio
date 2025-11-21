package com.fjjukic.zenvio.feature.walkthrough.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
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
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughIntent
import com.fjjukic.zenvio.ui.theme.DividerLight
import com.fjjukic.zenvio.ui.theme.ZenvioTheme

@Composable
fun WalkthroughScreen(
    viewModel: WalkthroughViewModel = hiltViewModel(),
    onFinished: () -> Unit = {}
) {
    CustomSystemBars(lightStatusBarIcons = false, lightNavigationBarIcons = true)

    val activity = LocalContext.current.findActivity()
    val uiState by viewModel.uiState.collectAsState()

    val pageCount = uiState.pages.size
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pageCount }
    )

    BackHandler {
        viewModel.onIntent(WalkthroughIntent.BackPressed)
    }

    Scaffold(
        bottomBar = {
            WalkthroughBottomBar(
                modifier = Modifier.navigationBarsPadding(),
                pageSize = pageCount,
                pagerState = pagerState,
                startBtnText = uiState.startBtnText,
                endBtnText = uiState.endBtnText,
                onStartBtnClick = {
                    viewModel.onIntent(WalkthroughIntent.StartButtonClick)
                },
                onEndBtnClick = {
                    viewModel.onIntent(WalkthroughIntent.EndButtonClick)
                }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = true,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) { position ->
            val page = uiState.pages[position]
            WalkthroughPageScreen(page)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                WalkthroughViewModel.WalkthroughEffect.WalkthroughCanceled -> {
                    activity?.finish()
                }

                WalkthroughViewModel.WalkthroughEffect.WalkthroughFinished -> {
                    onFinished()
                }
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onIntent(WalkthroughIntent.PageChanged(pagerState.currentPage))
    }

    LaunchedEffect(uiState.currentPage) {
        pagerState.animateScrollToPage(uiState.currentPage)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun WalkthroughBottomBarPreview() {
    ZenvioTheme {
        WalkthroughBottomBar(
            pageSize = 4,
            pagerState = PagerState(currentPage = 0) { 4 },
            startBtnText = R.string.btn_back,
            endBtnText = R.string.btn_continue
        )
    }
}

@Composable
fun WalkthroughBottomBar(
    modifier: Modifier = Modifier,
    pageSize: Int,
    pagerState: PagerState,
    startBtnText: Int? = null,
    endBtnText: Int? = null,
    onStartBtnClick: () -> Unit = {},
    onEndBtnClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StretchIndicator(
            modifier = Modifier.padding(top = 24.dp),
            pagerState = pagerState,
            count = pageSize,
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
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
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

            if (endBtnText != null) {
                Button(
                    onClick = onEndBtnClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) { Text(stringResource(endBtnText)) }

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
val PagerState.pageOffset: Float
    get() = currentPage + currentPageOffsetFraction


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StretchIndicator(
    pagerState: PagerState,
    count: Int,
    modifier: Modifier = Modifier,
    width: Dp = 8.dp,
    height: Dp = 8.dp,
    activeLineWidth: Dp = 24.dp,
    circleSpacing: Dp = 8.dp,
    radius: Float = 50f
) {
    val totalWidth = (width * count) + (circleSpacing * (count - 1))
    val indicatorColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.width(totalWidth)) {
        val spacing = circleSpacing.toPx()
        val dotWidth = width.toPx()
        val dotHeight = height.toPx()
        val activeWidth = activeLineWidth.toPx()

        var x = 0f
        val y = center.y

        repeat(count) { i ->
            val pos = pagerState.pageOffset
            val offset = pos % 1
            val curr = pos.toInt()

            val factor = offset * (activeWidth - dotWidth)

            val w = when (i) {
                curr -> activeWidth - factor
                curr + 1 -> dotWidth + factor
                else -> dotWidth
            }

            drawRoundRect(
                color = indicatorColor,
                topLeft = androidx.compose.ui.geometry.Offset(x, y - dotHeight / 2),
                size = androidx.compose.ui.geometry.Size(w, dotHeight),
                cornerRadius = CornerRadius(radius)
            )

            x += w + spacing
        }
    }
}