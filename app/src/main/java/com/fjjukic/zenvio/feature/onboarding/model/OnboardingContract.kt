package com.fjjukic.zenvio.feature.onboarding.model

data class OnboardingStateUi(
    val currentStepIndex: Int = 0,
    val steps: List<OnboardingStep> = emptyList(),
    val isFinished: Boolean = false,
    val isLoading: Boolean = true
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

sealed interface OnboardingIntent {
    data class UpdateName(val value: String) : OnboardingIntent
    data class SelectGender(val value: GenderType) : OnboardingIntent
    data class SelectAge(val value: Int) : OnboardingIntent
    data class ToggleSelect(val id: Int) : OnboardingIntent
    data object Next : OnboardingIntent
    data object BackPressed : OnboardingIntent
}

sealed class OnboardingEffect {
    data object OnboardingCanceled : OnboardingEffect()
    data object OnboardingFinished : OnboardingEffect()
}