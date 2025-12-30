package com.fjjukic.zenvio.feature.home.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.fjjukic.zenvio.R

// Tabs at the bottom
enum class HomeTab(
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int
) {
    HOME(R.drawable.ic_nav_home, R.string.nav_home),
    EXPLORE(R.drawable.ic_nav_explore, R.string.nav_explore),
    SLEEP(R.drawable.ic_nav_sleep, R.string.nav_sleep),
    INSIGHTS(R.drawable.ic_nav_insights, R.string.nav_insights),
    ACCOUNT(R.drawable.ic_nav_account, R.string.nav_account)
}

// Mood row
enum class Mood(
    @DrawableRes val iconRes: Int,
    val emojiColor: Color
) {
    VERY_BAD(R.drawable.ic_mood_very_bad, Color(0xFFE4584C)),
    BAD(R.drawable.ic_mood_bad, Color(0xFFF39A3B)),
    NEUTRAL(R.drawable.ic_mood_neutral, Color(0xFF6D7A8A)),
    GOOD(R.drawable.ic_mood_good, Color(0xFF85B86A)),
    VERY_GOOD(R.drawable.ic_mood_very_good, Color(0xFF59B56B))
}

// Type label above each plan card
enum class PlanType(@StringRes val labelRes: Int) {
    MEDITATION(R.string.plan_type_meditation),
    ARTICLE(R.string.plan_type_articles),
    BREATHING(R.string.plan_type_breathing),
    JOURNAL(R.string.plan_type_smart_journal)
}

data class PlanItem(
    val id: String,
    val type: PlanType,
    @StringRes val titleRes: Int,
    @DrawableRes val illustrationRes: Int,
    val durationStart: Int,
    val durationEnd: Int? = null,
    val isCompleted: Boolean = false
)

data class HomeUiState(
    val userName: String = "",
    val selectedMood: Mood? = null,
    val moods: List<Mood> = Mood.entries.toList(),
    val todayPlans: List<PlanItem> = emptyList(),
    val completedCount: Int = 0
)

// Intents / events from UI
sealed interface HomeIntent {
    data class MoodSelected(val mood: Mood) : HomeIntent
    data class PlanClicked(val id: String) : HomeIntent
    data class BottomTabSelected(val tab: HomeTab) : HomeIntent
    data object ChatWithZenvioClicked : HomeIntent
    data object TalkWithCoachClicked : HomeIntent
    data object BannerClicked : HomeIntent
    data object SearchClicked : HomeIntent
}

// One time events
sealed interface HomeEffect {
    data class ShowToast(val messageRes: Int) : HomeEffect
    data object ShowChatScreen : HomeEffect
    data class NavigateTab(val tab: HomeTab) : HomeEffect
}
