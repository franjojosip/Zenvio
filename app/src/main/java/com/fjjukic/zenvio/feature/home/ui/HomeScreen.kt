package com.fjjukic.zenvio.feature.home.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.home.HomeViewModel
import com.fjjukic.zenvio.feature.home.model.ActionCard
import com.fjjukic.zenvio.feature.home.model.HomeEffect
import com.fjjukic.zenvio.feature.home.model.HomeIntent
import com.fjjukic.zenvio.feature.home.model.HomeTab
import com.fjjukic.zenvio.feature.home.model.HomeUiState
import com.fjjukic.zenvio.feature.home.model.MoodSelectorSection
import com.fjjukic.zenvio.feature.home.model.PlanTimelineItem
import com.fjjukic.zenvio.ui.defaults.AppInputDefaults
import com.fjjukic.zenvio.ui.theme.ZenvioTheme

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    showChatScreen: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.ShowToast -> {
                    Toast.makeText(
                        context,
                        context.getString(effect.messageRes),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                HomeEffect.ShowChatScreen -> {
                    showChatScreen()
                }
            }
        }
    }

    HomeScreen(
        state = state,
        onIntent = { intent ->
            viewModel.onIntent(intent)
        }
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onIntent: (HomeIntent) -> Unit
) {
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            HomeTopAppBar(modifier = Modifier.statusBarsPadding()) {
                onIntent(HomeIntent.SearchClicked)
            }
        },
        bottomBar = {
            HomeBottomBar(
                selectedTab = state.selectedTab,
                onTabSelected = { onIntent(HomeIntent.BottomTabSelected(it)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp)
                    .padding(top = 12.dp)
            ) {
                item {
                    HomeBanner(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = { onIntent(HomeIntent.BannerClicked) })
                }

                item {
                    MoodSelectorSection(
                        modifier = Modifier.padding(top = 16.dp),
                        selectedMood = state.selectedMood,
                        moods = state.moods,
                        onMoodSelected = { onIntent(HomeIntent.MoodSelected(it)) }
                    )
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        ActionCard(
                            title = stringResource(R.string.title_chat_with_zenvio),
                            imageRes = R.drawable.img_chat_mindypng,
                            modifier = Modifier.weight(1f),
                            onClick = { onIntent(HomeIntent.ChatWithMindyClicked) }
                        )
                        ActionCard(
                            title = stringResource(R.string.home_action_talk_with_coach),
                            imageRes = R.drawable.img_talk_with_coach,
                            modifier = Modifier.weight(1f),
                            onClick = { onIntent(HomeIntent.TalkWithCoachClicked) }
                        )
                    }
                }

                item {
                    Text(
                        text = stringResource(
                            R.string.home_plans_title,
                            state.completedCount,
                            state.todayPlans.size
                        ),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                    )
                }
                itemsIndexed(state.todayPlans) { index, plan ->
                    PlanTimelineItem(
                        plan = plan,
                        isFirst = index == 0,
                        isLast = index == state.todayPlans.lastIndex,
                        onClick = { onIntent(HomeIntent.PlanClicked(plan.id)) }
                    )
                }

                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun HomeTopAppBarPreview() {
    ZenvioTheme {
        HomeTopAppBar()
    }
}

@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 24.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_lotus),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )

        Text(
            text = stringResource(R.string.home_title),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        IconButton(
            onClick = {
                onSearchClick()
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                tint = AppInputDefaults.itemColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun HomeBottomBarPreview() {
    ZenvioTheme {
        HomeBottomBar(
            selectedTab = HomeTab.HOME,
            onTabSelected = {}
        )
    }
}

@Composable
private fun HomeBottomBar(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        HomeTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        painter = painterResource(tab.iconRes),
                        contentDescription = null,
                        tint = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            AppInputDefaults.navigationItemColor
                        }
                    )
                },
                label = {
                    Text(
                        text = stringResource(tab.labelRes),
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = AppInputDefaults.navigationItemColor,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = AppInputDefaults.navigationItemColor,
                    selectedIndicatorColor = Color.Transparent,
                    disabledIconColor = Color.Transparent,
                    disabledTextColor = Color.Transparent
                ),

                interactionSource = remember { MutableInteractionSource() },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun HomeBannerPreview() {
    ZenvioTheme {
        HomeBanner()
    }
}

@Composable
fun HomeBanner(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .height(150.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onClick()
                }
            )
    ) {
        Image(
            painter = painterResource(R.drawable.img_banner_introduction),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}

