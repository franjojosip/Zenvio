package com.fjjukic.zenvio.feature.sleep.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.home.model.HomeBottomBar
import com.fjjukic.zenvio.feature.home.model.HomeTab
import com.fjjukic.zenvio.feature.home.model.MainToolbar
import com.fjjukic.zenvio.feature.sleep.SleepViewModel
import com.fjjukic.zenvio.feature.sleep.domain.SleepRecommendationType
import com.fjjukic.zenvio.feature.sleep.model.SleepCard
import com.fjjukic.zenvio.feature.sleep.model.SleepCardType
import com.fjjukic.zenvio.feature.sleep.model.SleepEffect
import com.fjjukic.zenvio.feature.sleep.model.SleepIntent
import com.fjjukic.zenvio.feature.sleep.model.SleepUiState

@Composable
fun SleepScreen(
    viewModel: SleepViewModel = hiltViewModel(),
    onBreathingClick: () -> Unit,
    onNavigateBack: () -> Unit = {},
    onTabClick: (HomeTab) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SleepEffect.NavigateTab -> onTabClick(effect.tab)
                SleepEffect.NavigateBreathing -> onBreathingClick()
                SleepEffect.ShowComingSoon ->
                    Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()

                SleepEffect.NavigateBack -> onNavigateBack()
                SleepEffect.NavigateMeditation ->
                    Toast.makeText(context, "Sleep meditation not implemented", Toast.LENGTH_SHORT)
                        .show()

                SleepEffect.NavigateSound ->
                    Toast.makeText(context, "Soundscape not implemented", Toast.LENGTH_SHORT).show()
            }
        }
    }

    SleepScreenStateless(
        state = state,
        onIntent = { viewModel.onIntent(it) }
    )
}

@Composable
fun SleepScreenStateless(
    state: SleepUiState,
    onIntent: (SleepIntent) -> Unit
) {

    Scaffold(
        topBar = {
            MainToolbar(titleRes = R.string.nav_sleep, modifier = Modifier.statusBarsPadding()) {
                //onIntent(HomeIntent.SearchClicked)
            }
        },
        bottomBar = {
            HomeBottomBar(
                selectedTab = HomeTab.SLEEP,
                onTabSelected = {
                    onIntent(SleepIntent.BottomTabSelected(it))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            HeaderSection(
                greeting = state.greeting,
                onBack = { onIntent(SleepIntent.BackClicked) }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp)
                    .padding(top = 12.dp)
            ) {
                item {
                    RecommendedSection(state.recommendedType)
                    Spacer(Modifier.height(20.dp))
                }

                items(state.cards) { card ->
                    SleepCardItem(
                        card = card,
                        isRecommended = card.type.name == state.recommendedType.name,
                        onClick = {
                            when (card.type) {
                                SleepCardType.BREATHING -> onIntent(SleepIntent.BreathingClicked)
                                SleepCardType.MEDITATION -> onIntent(SleepIntent.MeditationClicked)
                                SleepCardType.SOUND -> onIntent(SleepIntent.SoundClicked)
                            }
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

    }
}

@Composable
private fun HeaderSection(
    greeting: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(top = 48.dp, bottom = 32.dp, start = 22.dp, end = 22.dp)
    ) {
        Column {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_navigate_back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Let's prepare your mind and body for deep rest.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun RecommendedSection(
    type: SleepRecommendationType
) {
    val text = when (type) {
        SleepRecommendationType.BREATHING -> "Recommended for better sleep: Breathing Exercise"
        SleepRecommendationType.MEDITATION -> "Recommended: Sleep Meditation"
        SleepRecommendationType.SOUND -> "Try this tonight: Sleep Soundscape"
    }

    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun SleepCardItem(
    card: SleepCard,
    isRecommended: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isRecommended)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                else
                    MaterialTheme.colorScheme.surface
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(card.iconRes),
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                text = stringResource(card.titleRes),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = card.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
