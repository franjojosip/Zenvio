package com.fjjukic.zenvio.core.data.profile

import androidx.datastore.core.DataStore
import com.fjjukic.zenvio.core.datastore.UserProfileProto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileManager @Inject constructor(
    private val dataStore: DataStore<UserProfileProto>
) {

    fun userProfile(): Flow<UserProfileProto> = dataStore.data

    suspend fun saveProfile(
        name: String,
        gender: String,
        age: Int,
        goals: List<String>,
        mentalHealthCauses: List<String>,
        stressFrequency: String,
        healthyEating: String,
        meditationExperience: String,
        sleepQuality: String,
        happiness: String
    ) {
        dataStore.updateData { current ->
            current.toBuilder()
                .setName(name)
                .setGender(gender)
                .setAge(age)
                .clearGoals().addAllGoals(goals)
                .clearMentalHealthCauses().addAllMentalHealthCauses(mentalHealthCauses)
                .setStressFrequency(stressFrequency)
                .setHealthyEating(healthyEating)
                .setMeditationExperience(meditationExperience)
                .setSleepQuality(sleepQuality)
                .setHappinessLevel(happiness)
                .build()
        }
    }
}
