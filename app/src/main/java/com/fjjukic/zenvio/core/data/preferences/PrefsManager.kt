// In file: core/data/preferences/PrefsManager.kt

package com.fjjukic.zenvio.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PrefsManager {
    fun isWalkthroughCompleted(): Flow<Boolean>
    fun isOnboardingCompleted(): Flow<Boolean>

    suspend fun setWalkthroughCompleted(completed: Boolean)
    suspend fun setOnboardingCompleted(completed: Boolean)
}

class DataStorePrefsManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PrefsManager {
    private object Keys {
        val WALKTHROUGH_COMPLETED = booleanPreferencesKey("walkthrough_completed")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    override fun isWalkthroughCompleted(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[Keys.WALKTHROUGH_COMPLETED] ?: false
        }
    }

    override fun isOnboardingCompleted(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[Keys.ONBOARDING_COMPLETED] ?: false
        }
    }

    override suspend fun setWalkthroughCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.WALKTHROUGH_COMPLETED] = completed
        }
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.ONBOARDING_COMPLETED] = completed
        }
    }
}