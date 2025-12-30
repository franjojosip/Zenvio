package com.fjjukic.zenvio.feature.sleep.domain

import com.fjjukic.zenvio.core.datastore.UserProfileProto
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

interface SleepContextManager {
    fun greetingMessage(name: String): String
    fun recommendedCard(profile: UserProfileProto): SleepRecommendationType
}

@Singleton
class SleepContextManagerImpl @Inject constructor() : SleepContextManager {

    override fun greetingMessage(name: String): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val base = when (hour) {
            in 5..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            else -> "Good evening"
        }

        return "$base, $name"
    }

    override fun recommendedCard(profile: UserProfileProto): SleepRecommendationType {
        // Simple intelligent rules based on user onboarding
        val stress = profile.stressFrequency.lowercase()
        val sleep = profile.sleepQuality.lowercase()
        val meditation = profile.meditationExperience.lowercase()

        return when {
            stress.contains("daily") || stress.contains("frequently") ->
                SleepRecommendationType.BREATHING

            sleep.contains("poor") || sleep.contains("very poor") ->
                SleepRecommendationType.MEDITATION

            meditation.contains("never") ->
                SleepRecommendationType.BREATHING

            else ->
                SleepRecommendationType.SOUND
        }
    }
}

enum class SleepRecommendationType {
    BREATHING, MEDITATION, SOUND
}