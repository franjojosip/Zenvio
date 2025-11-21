package com.fjjukic.zenvio.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.core.data.preferences.PrefsManager
import com.fjjukic.zenvio.core.data.repository.OnboardingRepository
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingIntent
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val sharedPrefsManager: PrefsManager,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {
    private val _effect = MutableSharedFlow<OnboardingEffect>()
    val effect: SharedFlow<OnboardingEffect> = _effect

    private val _state = MutableStateFlow(OnboardingStateUi())
    val state: StateFlow<OnboardingStateUi> = _state

    init {
        loadSteps()
    }

    private fun loadSteps() {
        viewModelScope.launch {
            val steps = onboardingRepository.getSteps()
            _state.value = OnboardingStateUi(
                steps = steps
            )
        }
    }

    fun onIntent(intent: OnboardingIntent) {
        viewModelScope.launch {
            val currentValue = state.value
            when (intent) {
                is OnboardingIntent.UpdateName -> {
                    val step = currentValue.currentStep as? OnboardingStep.Name ?: return@launch
                    updateStep(step.copy(name = intent.value))
                }

                is OnboardingIntent.SelectGender -> {
                    val step = currentValue.currentStep as? OnboardingStep.Gender ?: return@launch
                    val updatedGenders = step.genders.map {
                        it.copy(isSelected = it.genderType == intent.value)
                    }
                    updateStep(step.copy(genders = updatedGenders))
                }

                is OnboardingIntent.SelectAge -> {
                    val step = currentValue.currentStep as? OnboardingStep.Age ?: return@launch
                    updateStep(step.copy(age = intent.value))
                }

                is OnboardingIntent.ToggleSelect -> {
                    val step =
                        currentValue.currentStep as? OnboardingStep.ChoiceSelect ?: return@launch

                    val updatedChoices = step.choices.map { choice ->
                        when {
                            step.isMultiSelect && choice.id == intent.id -> {
                                choice.copy(isSelected = !choice.isSelected)
                            }

                            !step.isMultiSelect -> {
                                choice.copy(isSelected = choice.id == intent.id)
                            }

                            else -> choice
                        }
                    }
                    updateStep(step.copy(choices = updatedChoices))
                }

                OnboardingIntent.Next -> {
                    val currentStepIndex = currentValue.currentStepIndex
                    if (currentValue.isLastStep) {
                        finishOnboarding()
                    } else {
                        _state.value =
                            currentValue.copy(currentStepIndex = currentStepIndex + 1)
                    }

                }

                OnboardingIntent.BackPressed -> {
                    val currentStepIndex = currentValue.currentStepIndex
                    if (currentValue.canGoBack) {
                        _state.value =
                            currentValue.copy(currentStepIndex = currentStepIndex - 1)
                    } else {
                        _effect.emit(OnboardingEffect.OnboardingCanceled)
                    }
                }
            }
        }
    }

    private suspend fun finishOnboarding() {
        sharedPrefsManager.setOnboardingCompleted(true)
        _effect.emit(OnboardingEffect.OnboardingFinished)

    }

    private fun updateStep(newStep: OnboardingStep) {
        val index = _state.value.currentStepIndex
        val updated = _state.value.steps.toMutableList().also {
            it[index] = newStep
        }
        _state.value = _state.value.copy(steps = updated)
    }

    data class OnboardingStateUi(
        val currentStepIndex: Int = 0,
        val steps: List<OnboardingStep> = emptyList(),
        val isFinished: Boolean = false
    ) {
        val currentStep: OnboardingStep?
            get() = steps.getOrNull(currentStepIndex)

        val canGoBack
            get() = currentStepIndex > 0

        val isLastStep
            get() = currentStepIndex == steps.lastIndex

        val progress
            get() = (currentStepIndex + 1).toFloat() / steps.size
        val counterText
            get() = "${currentStepIndex + 1} / ${steps.size}"

        val isBtnEnabled: Boolean
            get() = when (val step = currentStep) {
                is OnboardingStep.Name -> step.isValid
                is OnboardingStep.Gender -> step.genders.any { it.isSelected }
                is OnboardingStep.Age -> step.isValid
                is OnboardingStep.ChoiceSelect -> step.isValid
                null -> true
            }
    }

    sealed class OnboardingEffect {
        data object OnboardingCanceled : OnboardingEffect()
        data object OnboardingFinished : OnboardingEffect()
    }

}