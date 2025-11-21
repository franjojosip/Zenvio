package com.fjjukic.zenvio.feature.onboarding.model

sealed interface OnboardingIntent {
    data class UpdateName(val value: String) : OnboardingIntent
    data class SelectGender(val value: GenderType) : OnboardingIntent
    data class SelectAge(val value: Int) : OnboardingIntent
    data class ToggleSelect(val id: Int) : OnboardingIntent
    data object Next : OnboardingIntent
    data object BackPressed : OnboardingIntent
}