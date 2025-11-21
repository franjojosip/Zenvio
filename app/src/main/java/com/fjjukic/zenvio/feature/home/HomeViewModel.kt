package com.fjjukic.zenvio.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect: SharedFlow<HomeEffect> = _effect

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        _state.value = _state.value.copy(
            todayPlans = listOf(
                PlanItem(
                    id = "1",
                    type = PlanType.MEDITATION,
                    titleRes = R.string.plan_intro_meditation,
                    durationStart = 480,
                    illustrationRes = R.drawable.img_meditation_intro
                ),
                PlanItem(
                    id = "2",
                    type = PlanType.ARTICLE,
                    titleRes = R.string.plan_mindfulness_techniques,
                    durationStart = 120,
                    illustrationRes = R.drawable.img_mindfulness_techinques
                ),
                PlanItem(
                    id = "3",
                    type = PlanType.BREATHING,
                    titleRes = R.string.plan_deep_breath_dynamics,
                    durationStart = 120,
                    durationEnd = 300,
                    illustrationRes = R.drawable.img_deep_breath
                ),
                PlanItem(
                    id = "4",
                    type = PlanType.JOURNAL,
                    titleRes = R.string.plan_smart_journal,
                    durationStart = 20,
                    durationEnd = 40,
                    illustrationRes = R.drawable.img_activities
                ),
                PlanItem(
                    id = "5",
                    type = PlanType.MEDITATION,
                    titleRes = R.string.plan_gratitude_meditation,
                    durationStart = 600,
                    illustrationRes = R.drawable.img_gratitude_meditation
                )
            )
        )
    }

    fun onIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when (intent) {
                is HomeIntent.MoodSelected -> {
                    _state.value = _state.value.copy(selectedMood = intent.mood)
                }

                is HomeIntent.PlanClicked -> {
                    // simple toggle completed for demo
                    val updated = _state.value.todayPlans.map {
                        if (it.id == intent.id) it.copy(isCompleted = !it.isCompleted) else it
                    }
                    _state.value = _state.value.copy(
                        todayPlans = updated,
                        completedCount = updated.count { it.isCompleted }
                    )
                }

                is HomeIntent.BottomTabSelected -> {
                    _state.value = _state.value.copy(selectedTab = intent.tab)
                }

                HomeIntent.ChatWithMindyClicked, HomeIntent.TalkWithCoachClicked, HomeIntent.BannerClicked, HomeIntent.SearchClicked -> {
                    _effect.emit(HomeEffect.ShowToast(R.string.action_not_implemented))
                }
            }

        }
    }

    sealed class HomeEffect {
        data class ShowToast(val messageRes: Int) : HomeEffect()
    }
}
