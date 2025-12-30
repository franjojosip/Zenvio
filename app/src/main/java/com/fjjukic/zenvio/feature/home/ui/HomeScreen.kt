package com.fjjukic.zenvio.feature.home.ui

import android.content.Context
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.home.HomeViewModel
import com.fjjukic.zenvio.feature.home.model.ActionCard
import com.fjjukic.zenvio.feature.home.model.HomeBottomBar
import com.fjjukic.zenvio.feature.home.model.HomeEffect
import com.fjjukic.zenvio.feature.home.model.HomeIntent
import com.fjjukic.zenvio.feature.home.model.HomeTab
import com.fjjukic.zenvio.feature.home.model.HomeUiState
import com.fjjukic.zenvio.feature.home.model.MainToolbar
import com.fjjukic.zenvio.feature.home.model.MoodSelectorSection
import com.fjjukic.zenvio.feature.home.model.PlanTimelineItem
import com.fjjukic.zenvio.ui.theme.ZenvioTheme
import java.util.Calendar

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    showChatScreen: () -> Unit = {},
    onTabClick: (HomeTab) -> Unit = {}
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

                HomeEffect.ShowChatScreen -> showChatScreen()
                is HomeEffect.NavigateTab -> onTabClick(effect.tab)
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
            MainToolbar(titleRes = R.string.nav_home, modifier = Modifier.statusBarsPadding()) {
                onIntent(HomeIntent.SearchClicked)
            }
        },
        bottomBar = {
            HomeBottomBar(
                selectedTab = HomeTab.HOME,
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
                    HomeGreeting(
                        name = state.userName,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
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
                            imageRes = R.drawable.img_chat_zenvio,
                            modifier = Modifier.weight(1f),
                            onClick = { onIntent(HomeIntent.ChatWithZenvioClicked) }
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

@Composable
fun HomeGreeting(
    name: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Good ${currentGreeting(context)}, $name 👋",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.how_are_you_feeling_today),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

fun currentGreeting(context: Context): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour < 12 -> context.getString(R.string.morning)
        hour < 17 -> context.getString(R.string.afternoon)
        else -> context.getString(R.string.evening)
    }
}
