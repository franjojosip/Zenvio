package com.fjjukic.zenvio.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.core.data.preferences.PrefsManager
import com.fjjukic.zenvio.feature.onboarding.data.repository.OnboardingRepository
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingEffect
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingIntent
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingStateUi
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val sharedPrefsManager: PrefsManager,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {
    private val _effect = MutableSharedFlow<OnboardingEffect>()
    val effect = _effect.asSharedFlow()

    private val _state = MutableStateFlow(OnboardingStateUi())
    val state = _state.asStateFlow()

    init {
        loadSteps()
    }

    private fun loadSteps() {
        viewModelScope.launch {
            val steps = onboardingRepository.getSteps()
            _state.value = OnboardingStateUi(
                steps = steps,
                isLoading = false
            )
        }
    }

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.UpdateName -> {
                val step = _state.value.currentStep as? OnboardingStep.Name ?: return
                updateStep(step.copy(name = intent.value))
            }

            is OnboardingIntent.SelectGender -> {
                val step = _state.value.currentStep as? OnboardingStep.Gender ?: return
                val updatedGenders = step.genders.map {
                    it.copy(isSelected = it.genderType == intent.value)
                }
                updateStep(step.copy(genders = updatedGenders))
            }

            is OnboardingIntent.SelectAge -> {
                val step = _state.value.currentStep as? OnboardingStep.Age ?: return
                updateStep(step.copy(age = intent.value))
            }

            is OnboardingIntent.ToggleSelect -> {
                val step = _state.value.currentStep as? OnboardingStep.ChoiceSelect ?: return
                val updatedChoices = step.choices.map { choice ->
                    when {
                        step.isMultiSelect && choice.id == intent.id -> choice.copy(isSelected = !choice.isSelected)
                        !step.isMultiSelect -> choice.copy(isSelected = choice.id == intent.id)
                        else -> choice
                    }
                }
                updateStep(step.copy(choices = updatedChoices))
            }

            OnboardingIntent.Next -> {
                if (_state.value.isLastStep) {
                    finishOnboarding()
                } else {
                    _state.update { it.copy(currentStepIndex = it.currentStepIndex + 1) }
                }
            }

            OnboardingIntent.BackPressed -> {
                if (_state.value.canGoBack) {
                    _state.update { it.copy(currentStepIndex = it.currentStepIndex - 1) }
                } else {
                    viewModelScope.launch {
                        _effect.emit(OnboardingEffect.OnboardingCanceled)
                    }
                }
            }
        }
    }

    private fun finishOnboarding() {
        viewModelScope.launch {
            sharedPrefsManager.setOnboardingCompleted(true)
            _effect.emit(OnboardingEffect.OnboardingFinished)
        }
    }

    private fun updateStep(newStep: OnboardingStep) {
        _state.update { currentState ->
            val updatedSteps = currentState.steps.toMutableList()
                .apply { set(currentState.currentStepIndex, newStep) }
            currentState.copy(steps = updatedSteps)
        }
    }
}