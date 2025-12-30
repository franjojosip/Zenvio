package com.fjjukic.zenvio.feature.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.core.data.profile.UserProfileManager
import com.fjjukic.zenvio.feature.sleep.domain.SleepContextManager
import com.fjjukic.zenvio.feature.sleep.model.SleepEffect
import com.fjjukic.zenvio.feature.sleep.model.SleepIntent
import com.fjjukic.zenvio.feature.sleep.model.SleepUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SleepViewModel @Inject constructor(
    private val userProfileManager: UserProfileManager,
    private val contextManager: SleepContextManager
) : ViewModel() {

    private val _state = MutableStateFlow(SleepUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SleepEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeUserProfile()
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            userProfileManager.userProfile().collect { profile ->
                val greeting = contextManager.greetingMessage(profile.name)
                val recommendation = contextManager.recommendedCard(profile)

                _state.update {
                    it.copy(
                        greeting = greeting,
                        recommendedType = recommendation
                    )
                }
            }
        }
    }

    fun onIntent(intent: SleepIntent) {
        when (intent) {
            SleepIntent.BreathingClicked ->
                emit { SleepEffect.NavigateBreathing }

            SleepIntent.MeditationClicked ->
                emit { SleepEffect.NavigateMeditation }

            SleepIntent.SoundClicked ->
                emit { SleepEffect.NavigateSound }

            SleepIntent.BackClicked ->
                emit { SleepEffect.NavigateBack }

            is SleepIntent.BottomTabSelected -> {
                emit { SleepEffect.NavigateTab(intent.tab) }
            }
        }
    }

    private fun emit(builder: () -> SleepEffect) {
        viewModelScope.launch { _effect.emit(builder()) }
    }
}
