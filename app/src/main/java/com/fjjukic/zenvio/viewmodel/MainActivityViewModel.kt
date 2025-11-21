package com.fjjukic.zenvio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.core.data.preferences.PrefsManager
import com.fjjukic.zenvio.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val prefs: PrefsManager
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    init {
        viewModelScope.launch {
            val isOnboardingCompleted = prefs.isOnboardingCompleted()
            val isWalkthroughCompleted = prefs.isWalkthroughCompleted()

            _startDestination.value = if (!isWalkthroughCompleted) {
                Screen.Prelogin.route
            } else if (!isOnboardingCompleted) {
                Screen.Onboarding.route
            } else {
                Screen.Home.route
            }
        }
    }
}