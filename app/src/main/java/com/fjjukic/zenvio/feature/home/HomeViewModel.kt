package com.fjjukic.zenvio.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.home.data.repository.HomeRepository
import com.fjjukic.zenvio.feature.home.model.HomeEffect
import com.fjjukic.zenvio.feature.home.model.HomeIntent
import com.fjjukic.zenvio.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect = _effect.asSharedFlow()

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        _state.value = _state.value.copy(
            todayPlans = homeRepository.getTodayPlans()
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

                HomeIntent.ChatWithZenvioClicked -> {
                    _effect.emit(HomeEffect.ShowChatScreen)
                }

                HomeIntent.ChatWithZenvioClicked, HomeIntent.TalkWithCoachClicked, HomeIntent.BannerClicked, HomeIntent.SearchClicked -> {
                    _effect.emit(HomeEffect.ShowToast(R.string.action_not_implemented))
                }
            }

        }
    }
}
