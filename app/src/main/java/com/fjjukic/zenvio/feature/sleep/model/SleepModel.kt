package com.fjjukic.zenvio.feature.sleep.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.home.model.HomeTab
import com.fjjukic.zenvio.feature.sleep.domain.SleepRecommendationType

data class SleepUiState(
    val greeting: String = "Good evening",
    val recommendedType: SleepRecommendationType = SleepRecommendationType.MEDITATION,
    val cards: List<SleepCard> = listOf(
        SleepCard(
            titleRes = R.string.plan_deep_breath_dynamics,
            subtitle = "Relax your body with paced breathing.",
            iconRes = R.drawable.img_deep_breath,
            type = SleepCardType.BREATHING
        ),
        SleepCard(
            titleRes = R.string.plan_gratitude_meditation,
            subtitle = "Ease your thoughts with a calming meditation.",
            iconRes = R.drawable.img_gratitude_meditation,
            type = SleepCardType.MEDITATION
        ),
        SleepCard(
            titleRes = R.string.plan_mindfulness_techniques,
            subtitle = "Ambient soundscapes for deeper sleep.",
            iconRes = R.drawable.img_mindfulness_techinques,
            type = SleepCardType.SOUND
        )
    )
)

enum class SleepCardType {
    BREATHING, MEDITATION, SOUND
}

data class SleepCard(
    @StringRes val titleRes: Int,
    val subtitle: String,
    @DrawableRes val iconRes: Int,
    val type: SleepCardType
)

sealed interface SleepIntent {
    data object BreathingClicked : SleepIntent
    data object MeditationClicked : SleepIntent
    data object SoundClicked : SleepIntent
    data object BackClicked : SleepIntent
    data class BottomTabSelected(val tab: HomeTab) : SleepIntent
}

sealed interface SleepEffect {
    data class NavigateTab(val tab: HomeTab) : SleepEffect
    data object NavigateBreathing : SleepEffect
    data object NavigateMeditation : SleepEffect
    data object NavigateSound : SleepEffect
    data object NavigateBack : SleepEffect
    data object ShowComingSoon : SleepEffect
}
