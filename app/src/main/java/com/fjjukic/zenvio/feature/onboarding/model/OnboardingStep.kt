package com.fjjukic.zenvio.feature.onboarding.model

sealed class OnboardingStep {
    data class Name(
        val name: String = ""
    ) : OnboardingStep() {
        val isValid: Boolean
            get() = name.isNotBlank()
    }

    data class Gender(
        val genders: List<GenderUi>
    ) : OnboardingStep()

    data class Age(
        val age: Int = 25
    ) : OnboardingStep() {
        val isValid: Boolean = true
    }

    data class ChoiceSelect(
        val titleRes: Int,
        val subtitleRes: Int,
        val choices: List<SelectChoiceUi>,
        val isMultiSelect: Boolean = false
    ) : OnboardingStep() {
        val isValid: Boolean = choices.any { it.isSelected }
    }
}