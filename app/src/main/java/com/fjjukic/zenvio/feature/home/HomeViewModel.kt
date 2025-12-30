package com.fjjukic.zenvio.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.core.data.profile.UserProfileManager
import com.fjjukic.zenvio.core.datastore.UserProfileProto
import com.fjjukic.zenvio.feature.home.data.repository.HomeRepository
import com.fjjukic.zenvio.feature.home.model.HomeEffect
import com.fjjukic.zenvio.feature.home.model.HomeIntent
import com.fjjukic.zenvio.feature.home.model.HomeUiState
import com.fjjukic.zenvio.feature.home.model.PlanItem
import com.fjjukic.zenvio.feature.home.model.PlanType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val userProfileManager: UserProfileManager
) : ViewModel() {

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect = _effect.asSharedFlow()

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            userProfileManager.userProfile().collect { profile ->

                val allPlans = homeRepository.getTodayPlans()
                val personalizedOrder = reorderPlansForUser(allPlans, profile)

                _state.update {
                    it.copy(
                        userName = profile.name,
                        todayPlans = personalizedOrder,
                        completedCount = personalizedOrder.count { p -> p.isCompleted }
                    )
                }
            }
        }


    }

    private fun reorderPlansForUser(
        plans: List<PlanItem>,
        profile: UserProfileProto
    ): List<PlanItem> {
        val goals = profile.goalsList

        // Priority rules (simple + clear)
        val priorityType = when {
            goals.contains("Improve Sleep") -> PlanType.MEDITATION
            goals.contains("Reduce Stress") -> PlanType.BREATHING
            goals.contains("Boost Confidence") -> PlanType.JOURNAL
            else -> null
        }

        return if (priorityType == null) {
            plans // no personalization, keep original order
        } else {
            plans.sortedBy { plan ->
                if (plan.type == priorityType) 0 else 1
            }
        }
    }

    fun onIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when (intent) {
                is HomeIntent.MoodSelected -> {
                    _state.update { it.copy(selectedMood = intent.mood) }
                }

                is HomeIntent.PlanClicked -> {
                    val updated = _state.value.todayPlans.map {
                        if (it.id == intent.id) it.copy(isCompleted = !it.isCompleted)
                        else it
                    }

                    _state.update {
                        it.copy(
                            todayPlans = updated,
                            completedCount = updated.count { p -> p.isCompleted }
                        )
                    }
                }

                is HomeIntent.BottomTabSelected -> {
                    _effect.emit(HomeEffect.NavigateTab(intent.tab))
                }

                HomeIntent.ChatWithZenvioClicked -> {
                    _effect.emit(HomeEffect.ShowChatScreen)
                }

                HomeIntent.TalkWithCoachClicked,
                HomeIntent.BannerClicked,
                HomeIntent.SearchClicked -> {
                    _effect.emit(HomeEffect.ShowToast(R.string.action_not_implemented))
                }
            }
        }
    }
}