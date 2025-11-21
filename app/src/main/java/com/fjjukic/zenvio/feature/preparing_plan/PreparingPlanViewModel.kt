package com.fjjukic.zenvio.feature.preparing_plan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.feature.preparing_plan.model.PreparingPlanIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreparingPlanViewModel @Inject constructor() : ViewModel() {
    private val _effect = MutableSharedFlow<PreparingPlanEffect>()
    val effect: SharedFlow<PreparingPlanEffect> = _effect

    var uiState by mutableStateOf(PreparingPlanStateUi())
        private set

    fun onIntent(intent: PreparingPlanIntent) {
        when (intent) {
            PreparingPlanIntent.Start -> simulateProgress()
        }
    }

    private fun simulateProgress() {
        viewModelScope.launch {
            for (i in 0..100) {
                uiState = uiState.copy(progress = i)
                delay(35)
            }
            _effect.emit(PreparingPlanEffect.OnFinished)
        }
    }

    data class PreparingPlanStateUi(
        val progress: Int = 0 // 0–100
    )

    sealed class PreparingPlanEffect {
        data object OnFinished : PreparingPlanEffect()
    }

}
