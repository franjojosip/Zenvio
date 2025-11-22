package com.fjjukic.zenvio.feature.onboarding.model

import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fjjukic.zenvio.R

sealed class OnboardingStep {
    @get:StringRes
    abstract val titleRes: Int

    @get:StringRes
    abstract val subtitleRes: Int

    abstract val isValid: Boolean

    data class Name(
        val name: String = ""
    ) : OnboardingStep() {
        override val titleRes: Int = R.string.title_onboarding_name_step
        override val subtitleRes: Int = R.string.subtitle_onboarding_name_step
        override val isValid: Boolean get() = name.isNotBlank()
    }

    data class Gender(
        val genders: List<GenderUi>
    ) : OnboardingStep() {
        override val titleRes: Int = R.string.title_onboarding_gender_step
        override val subtitleRes: Int = R.string.subtitle_onboarding_gender_step
        override val isValid: Boolean get() = genders.any { it.isSelected }
    }

    data class Age(
        val age: Int = 25,
        val visibleItemCount: Int = 7,
        val itemHeight: Dp = 60.dp,
    ) : OnboardingStep() {
        override val titleRes: Int = R.string.title_onboarding_age_step
        override val subtitleRes: Int = R.string.subtitle_onboarding_age_step
        override val isValid: Boolean = true
    }

    data class ChoiceSelect(
        val choices: List<SelectChoiceUi>,
        val isMultiSelect: Boolean = false,
        @StringRes private val actualTitleRes: Int,
        @StringRes private val actualSubtitleRes: Int

    ) : OnboardingStep() {
        override val titleRes: Int get() = actualTitleRes
        override val subtitleRes: Int get() = actualSubtitleRes

        override val isValid: Boolean
            get() = choices.any { it.isSelected }
    }
}