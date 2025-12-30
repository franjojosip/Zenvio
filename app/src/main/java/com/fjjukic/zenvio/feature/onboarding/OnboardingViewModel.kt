package com.fjjukic.zenvio.feature.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.core.data.preferences.DataStorePrefsManager
import com.fjjukic.zenvio.core.data.profile.UserProfileManager
import com.fjjukic.zenvio.feature.onboarding.data.repository.OnboardingRepository
import com.fjjukic.zenvio.feature.onboarding.model.GenderType
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
    application: Application,
    private val sharedPrefsManager: DataStorePrefsManager,
    private val onboardingRepository: OnboardingRepository,
    private val userProfileManager: UserProfileManager
) : AndroidViewModel(application) {
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
            _state.update {
                it.copy(
                    steps = steps,
                    isLoading = false
                )
            }
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
            val name = getName()
            val gender = getGender()
            val age = getAge()
            val goals = getGoals()
            val causes = getMentalHealthCauses()
            val stress = getStressFrequency()
            val eating = getHealthyEating()
            val meditation = getMeditationExperience()
            val sleep = getSleepQuality()
            val happiness = getHappiness()

            userProfileManager.saveProfile(
                name = name,
                gender = gender,
                age = age,
                goals = goals,
                mentalHealthCauses = causes,
                stressFrequency = stress,
                healthyEating = eating,
                meditationExperience = meditation,
                sleepQuality = sleep,
                happiness = happiness
            )

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

    private val appContext: Application get() = getApplication()

    private fun getName(): String {
        return (_state.value.steps[0] as OnboardingStep.Name).name.trim()
    }

    private fun getGender(): String {
        val step = _state.value.steps[1] as OnboardingStep.Gender
        val selected = step.genders.firstOrNull { it.isSelected }?.genderType ?: return ""

        return when (selected) {
            GenderType.MALE -> "Male"
            GenderType.FEMALE -> "Female"
            GenderType.OTHER -> "Other"
        }
    }

    private fun getAge(): Int {
        return (_state.value.steps[2] as OnboardingStep.Age).age
    }

    private fun getGoals(): List<String> {
        val step = _state.value.steps[3] as OnboardingStep.ChoiceSelect
        return step.choices
            .filter { it.isSelected }
            .map { appContext.getString(it.textRes) }
    }

    private fun getMentalHealthCauses(): List<String> {
        val step = _state.value.steps[4] as OnboardingStep.ChoiceSelect
        return step.choices
            .filter { it.isSelected }
            .map { appContext.getString(it.textRes) }
    }

    private fun getStressFrequency(): String {
        val step = _state.value.steps[5] as OnboardingStep.ChoiceSelect
        return step.choices.firstOrNull { it.isSelected }
            ?.let { appContext.getString(it.textRes) }
            ?: ""
    }

    private fun getHealthyEating(): String {
        val step = _state.value.steps[6] as OnboardingStep.ChoiceSelect
        return step.choices.firstOrNull { it.isSelected }
            ?.let { appContext.getString(it.textRes) }
            ?: ""
    }

    private fun getMeditationExperience(): String {
        val step = _state.value.steps[7] as OnboardingStep.ChoiceSelect
        return step.choices.firstOrNull { it.isSelected }
            ?.let { appContext.getString(it.textRes) }
            ?: ""
    }

    private fun getSleepQuality(): String {
        val step = _state.value.steps[8] as OnboardingStep.ChoiceSelect
        return step.choices.firstOrNull { it.isSelected }
            ?.let { appContext.getString(it.textRes) }
            ?: ""
    }

    private fun getHappiness(): String {
        val step = _state.value.steps[9] as OnboardingStep.ChoiceSelect
        return step.choices.firstOrNull { it.isSelected }
            ?.let { appContext.getString(it.textRes) }
            ?: ""
    }
}