package com.fjjukic.zenvio.core.data.repository

import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingStep
import com.fjjukic.zenvio.feature.onboarding.model.SelectChoiceUi
import com.fjjukic.zenvio.feature.onboarding.model.getGenders
import javax.inject.Inject

interface OnboardingRepository {
    fun getSteps(): List<OnboardingStep>
}

class OnboardingRepositoryImpl @Inject constructor() : OnboardingRepository {
    override fun getSteps(): List<OnboardingStep> {
        return listOf(
            OnboardingStep.Name(),
            OnboardingStep.Gender(
                genders = getGenders()
            ),
            OnboardingStep.Age(),
            OnboardingStep.ChoiceSelect(
                titleRes = R.string.title_onboarding_main_goals_step,
                subtitleRes = R.string.subtitle_onboarding_main_goals_step,
                choices = mapGoalChoices(),
                isMultiSelect = true
            ),
            OnboardingStep.ChoiceSelect(
                titleRes = R.string.title_onboarding_mental_health_step,
                subtitleRes = R.string.subtitle_onboarding_mental_health_step,
                choices = mapHealthIssueAreaChoices(),
            ),
            OnboardingStep.ChoiceSelect(
                titleRes = R.string.title_onboarding_stress_feel_step,
                subtitleRes = R.string.subtitle_onboarding_stress_feel_step,
                choices = mapAnxietyFrequencyChoices()
            ),
            OnboardingStep.ChoiceSelect(
                titleRes = R.string.title_onboarding_eat_healthy_step,
                subtitleRes = R.string.subtitle_onboarding_eat_healthy_step,
                choices = mapHealthConsistencyChoices()
            ),
            OnboardingStep.ChoiceSelect(
                titleRes = R.string.title_onboarding_try_meditation_step,
                subtitleRes = R.string.subtitle_onboarding_try_meditation_step,
                choices = mapMeditationExperienceChoices()
            ),
            OnboardingStep.ChoiceSelect(
                titleRes = R.string.title_onboarding_sleep_quality_step,
                subtitleRes = R.string.subtitle_onboarding_sleep_quality_step,
                choices = mapSleepQualityChoices()
            ),
            OnboardingStep.ChoiceSelect(
                titleRes = R.string.title_onboarding_rate_happiness_step,
                subtitleRes = R.string.subtitle_onboarding_rate_happiness_step,
                choices = mapHappinessChoices()
            )
        )
    }

    private fun mapGoalChoices(): List<SelectChoiceUi> {
        return getGoalChoices().mapIndexed { index, textRes ->
            SelectChoiceUi(index, textRes, false)
        }
    }

    private fun mapHealthIssueAreaChoices(): List<SelectChoiceUi> {
        return getHealthIssueAreaChoices().mapIndexed { index, textRes ->
            SelectChoiceUi(index, textRes, false)
        }
    }

    private fun mapAnxietyFrequencyChoices(): List<SelectChoiceUi> {
        return getAnxietyFrequencyChoices().mapIndexed { index, textRes ->
            SelectChoiceUi(index, textRes, false)
        }
    }

    private fun mapHealthConsistencyChoices(): List<SelectChoiceUi> {
        return getHealthConsistencyChoices().mapIndexed { index, textRes ->
            SelectChoiceUi(index, textRes, false)
        }
    }

    private fun mapMeditationExperienceChoices(): List<SelectChoiceUi> {
        return getMeditationExperienceChoices().mapIndexed { index, textRes ->
            SelectChoiceUi(index, textRes, false)
        }
    }

    private fun mapSleepQualityChoices(): List<SelectChoiceUi> {
        return getSleepQualityChoices().mapIndexed { index, textRes ->
            SelectChoiceUi(index, textRes, false)
        }
    }

    private fun mapHappinessChoices(): List<SelectChoiceUi> {
        return getHappinessChoices().mapIndexed { index, textRes ->
            SelectChoiceUi(index, textRes, false)
        }
    }

    private fun getGoalChoices(): List<Int> = listOf(
        R.string.choice_manage_anxiety,
        R.string.choice_reduce_stress,
        R.string.choice_improve_mood,
        R.string.choice_improve_sleep,
        R.string.choice_enhance_relationships,
        R.string.choice_boost_confidence
    )

    private fun getHealthIssueAreaChoices(): List<Int> = listOf(
        R.string.choice_work_school,
        R.string.choice_relationships,
        R.string.choice_finances,
        R.string.choice_health_concerns,
        R.string.choice_life_changes,
        R.string.choice_other
    )

    private fun getAnxietyFrequencyChoices(): List<Int> = listOf(
        R.string.choice_almost_daily,
        R.string.choice_frequently,
        R.string.choice_occasionally,
        R.string.choice_rarely,
        R.string.choice_never
    )

    private fun getHealthConsistencyChoices(): List<Int> = listOf(
        R.string.choice_always,
        R.string.choice_most_of_the_time,
        R.string.choice_sometimes,
        R.string.choice_rarely,
        R.string.choice_never
    )

    private fun getMeditationExperienceChoices(): List<Int> = listOf(
        R.string.choice_yes_regularly,
        R.string.choice_yes_occasionally,
        R.string.choice_yes_long_time_ago,
        R.string.choice_no_never
    )

    private fun getSleepQualityChoices(): List<Int> = listOf(
        R.string.choice_very_poor,
        R.string.choice_poor,
        R.string.choice_average,
        R.string.choice_good,
        R.string.choice_excellent
    )

    private fun getHappinessChoices(): List<Int> = listOf(
        R.string.choice_very_unhappy,
        R.string.choice_unhappy,
        R.string.choice_neutral,
        R.string.choice_happy,
        R.string.choice_very_happy
    )
}