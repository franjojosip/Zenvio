package com.fjjukic.zenvio.feature.onboarding.data.repository

import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.onboarding.data.datasource.OnboardingLocalDataSource
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingStep
import com.fjjukic.zenvio.feature.onboarding.model.SelectChoiceUi
import com.fjjukic.zenvio.feature.onboarding.model.mapGendersToUiModels
import javax.inject.Inject

interface OnboardingRepository {
    fun getSteps(): List<OnboardingStep>
}

class OnboardingRepositoryImpl @Inject constructor(
    private val localDataSource: OnboardingLocalDataSource
) : OnboardingRepository {
    override fun getSteps(): List<OnboardingStep> {
        return listOf(
            OnboardingStep.Name(),
            OnboardingStep.Gender(
                genders = mapGendersToUiModels() // This already handles its own mapping
            ),
            OnboardingStep.Age(),
            OnboardingStep.ChoiceSelect(
                actualTitleRes = R.string.title_onboarding_main_goals_step,
                actualSubtitleRes = R.string.subtitle_onboarding_main_goals_step,
                choices = mapChoices(localDataSource.getGoalChoices()), // Pass the map here
                isMultiSelect = true
            ),
            OnboardingStep.ChoiceSelect(
                actualTitleRes = R.string.title_onboarding_mental_health_step,
                actualSubtitleRes = R.string.subtitle_onboarding_mental_health_step,
                choices = mapChoices(localDataSource.getHealthIssueAreaChoices()),
                isMultiSelect = true // This should also be multi-select based on the subtitle
            ),
            OnboardingStep.ChoiceSelect(
                actualTitleRes = R.string.title_onboarding_stress_feel_step,
                actualSubtitleRes = R.string.subtitle_onboarding_stress_feel_step,
                choices = mapChoices(localDataSource.getAnxietyFrequencyChoices())
            ),
            OnboardingStep.ChoiceSelect(
                actualTitleRes = R.string.title_onboarding_eat_healthy_step,
                actualSubtitleRes = R.string.subtitle_onboarding_eat_healthy_step,
                choices = mapChoices(localDataSource.getHealthConsistencyChoices())
            ),
            OnboardingStep.ChoiceSelect(
                actualTitleRes = R.string.title_onboarding_try_meditation_step,
                actualSubtitleRes = R.string.subtitle_onboarding_try_meditation_step,
                choices = mapChoices(localDataSource.getMeditationExperienceChoices())
            ),
            OnboardingStep.ChoiceSelect(
                actualTitleRes = R.string.title_onboarding_sleep_quality_step,
                actualSubtitleRes = R.string.subtitle_onboarding_sleep_quality_step,
                choices = mapChoices(localDataSource.getSleepQualityChoices())
            ),
            OnboardingStep.ChoiceSelect(
                actualTitleRes = R.string.title_onboarding_rate_happiness_step,
                actualSubtitleRes = R.string.subtitle_onboarding_rate_happiness_step,
                choices = mapChoices(localDataSource.getHappinessChoices())
            )
        )
    }

    private fun mapChoices(choicesMap: Map<Int, Int>): List<SelectChoiceUi> {
        return choicesMap.entries.mapIndexed { index, entry ->
            SelectChoiceUi(
                id = index,
                textRes = entry.key,
                cdTextRes = entry.value,
                isSelected = false
            )
        }
    }
}
