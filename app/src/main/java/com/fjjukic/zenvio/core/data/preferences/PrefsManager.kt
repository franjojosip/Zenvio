package com.fjjukic.zenvio.core.data.preferences

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface PrefsManager {
    fun setOnboardingCompleted(value: Boolean)
    fun isOnboardingCompleted(): Boolean

    fun setWalkthroughCompleted(value: Boolean)
    fun isWalkthroughCompleted(): Boolean
}

class PrefsManagerImpl @Inject constructor(
    @ApplicationContext context: Context
) : PrefsManager {
    private val prefs = context.getSharedPreferences(ZENVIO_PREFS, Context.MODE_PRIVATE)

    override fun setOnboardingCompleted(value: Boolean) {
        prefs.edit { putBoolean(ONBOARDING_DONE, value) }
    }

    override fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(ONBOARDING_DONE, false)
    }

    override fun setWalkthroughCompleted(value: Boolean) {
        prefs.edit { putBoolean(WALKTHROUGH_DONE, value) }
    }

    override fun isWalkthroughCompleted(): Boolean {
        return prefs.getBoolean(WALKTHROUGH_DONE, false)
    }

    companion object {
        private const val ZENVIO_PREFS = "zenvio_prefs"
        private const val ONBOARDING_DONE = "onboarding_done"
        private const val WALKTHROUGH_DONE = "walkthrough_done"
    }
}