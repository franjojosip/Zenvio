package com.fjjukic.zenvio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.core.data.preferences.DataStorePrefsManager
import com.fjjukic.zenvio.core.navigation.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    prefs: DataStorePrefsManager
) : ViewModel() {

    val startGraph: StateFlow<String?> = combine(
        prefs.isWalkthroughCompleted(),
        prefs.isOnboardingCompleted()
    ) { isWalkthroughCompleted, isOnboardingCompleted ->
        when {
            !isWalkthroughCompleted -> Graph.PRELOGIN
            !isOnboardingCompleted -> Graph.ONBOARDING
            else -> Graph.MAIN
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
}