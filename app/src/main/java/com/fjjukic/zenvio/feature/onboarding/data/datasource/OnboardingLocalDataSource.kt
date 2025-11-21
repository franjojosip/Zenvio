package com.fjjukic.zenvio.feature.onboarding.data.datasource

import com.fjjukic.zenvio.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingLocalDataSource @Inject constructor() {
    /**
     * Returns a map of choices for main goals.
     * Key: String resource for the choice text.
     * Value: String resource for the content description.
     */
    fun getGoalChoices(): Map<Int, Int> = mapOf(
        R.string.choice_manage_anxiety to R.string.cd_choice_manage_anxiety,
        R.string.choice_reduce_stress to R.string.cd_choice_reduce_stress,
        R.string.choice_improve_mood to R.string.cd_choice_improve_mood,
        R.string.choice_improve_sleep to R.string.cd_choice_improve_sleep,
        R.string.choice_enhance_relationships to R.string.cd_choice_enhance_relationships,
        R.string.choice_boost_confidence to R.string.cd_choice_boost_confidence
    )

    /**
     * Returns a map of choices for mental health issue areas.
     */
    fun getHealthIssueAreaChoices(): Map<Int, Int> = mapOf(
        R.string.choice_work_school to R.string.cd_choice_work_school,
        R.string.choice_relationships to R.string.cd_choice_relationships,
        R.string.choice_finances to R.string.cd_choice_finances,
        R.string.choice_health_concerns to R.string.cd_choice_health_concerns,
        R.string.choice_life_changes to R.string.cd_choice_life_changes,
        R.string.choice_other to R.string.cd_choice_other
    )

    /**
     * Returns a map of choices for anxiety frequency.
     */
    fun getAnxietyFrequencyChoices(): Map<Int, Int> = mapOf(
        R.string.choice_almost_daily to R.string.cd_choice_almost_daily,
        R.string.choice_frequently to R.string.cd_choice_frequently,
        R.string.choice_occasionally to R.string.cd_choice_occasionally,
        R.string.choice_rarely to R.string.cd_choice_rarely,
        R.string.choice_never to R.string.cd_choice_never
    )

    /**
     * Returns a map of choices for healthy eating consistency.
     */
    fun getHealthConsistencyChoices(): Map<Int, Int> = mapOf(
        R.string.choice_always to R.string.cd_choice_always,
        R.string.choice_most_of_the_time to R.string.cd_choice_most_of_the_time,
        R.string.choice_sometimes to R.string.cd_choice_sometimes,
        R.string.choice_rarely to R.string.cd_choice_rarely,
        R.string.choice_never to R.string.cd_choice_never
    )

    /**
     * Returns a map of choices for meditation experience.
     */
    fun getMeditationExperienceChoices(): Map<Int, Int> = mapOf(
        R.string.choice_yes_regularly to R.string.cd_choice_yes_regularly,
        R.string.choice_yes_occasionally to R.string.cd_choice_yes_occasionally,
        R.string.choice_yes_long_time_ago to R.string.cd_choice_yes_long_time_ago,
        R.string.choice_no_never to R.string.cd_choice_no_never
    )

    /**
     * Returns a map of choices for sleep quality ratings.
     */
    fun getSleepQualityChoices(): Map<Int, Int> = mapOf(
        R.string.choice_very_poor to R.string.cd_choice_very_poor,
        R.string.choice_poor to R.string.cd_choice_poor,
        R.string.choice_average to R.string.cd_choice_average,
        R.string.choice_good to R.string.cd_choice_good,
        R.string.choice_excellent to R.string.cd_choice_excellent
    )

    /**
     * Returns a map of choices for happiness level ratings.
     */
    fun getHappinessChoices(): Map<Int, Int> = mapOf(
        R.string.choice_very_unhappy to R.string.cd_choice_very_unhappy,
        R.string.choice_unhappy to R.string.cd_choice_unhappy,
        R.string.choice_neutral to R.string.cd_choice_neutral,
        R.string.choice_happy to R.string.cd_choice_happy,
        R.string.choice_very_happy to R.string.cd_choice_very_happy
    )
}