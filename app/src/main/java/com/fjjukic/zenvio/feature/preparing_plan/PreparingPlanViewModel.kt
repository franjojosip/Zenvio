package com.fjjukic.zenvio.feature.preparing_plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.feature.preparing_plan.model.PreparingPlanEffect
import com.fjjukic.zenvio.feature.preparing_plan.model.PreparingPlanIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreparingPlanViewModel @Inject constructor() : ViewModel() {
    private val _effect = MutableSharedFlow<PreparingPlanEffect>()
    val effect = _effect.asSharedFlow()

    private var progressJob: Job? = null

    fun onIntent(intent: PreparingPlanIntent) {
        when (intent) {
            PreparingPlanIntent.Start -> simulateProcessDuration()
        }
    }

    private fun simulateProcessDuration() {
        if (progressJob?.isActive == true) return

        progressJob = viewModelScope.launch {
            delay(TOTAL_PREPARATION_TIME_MS)
            _effect.emit(PreparingPlanEffect.OnFinished)
        }
    }

    override fun onCleared() {
        progressJob?.cancel()
        super.onCleared()
    }

    companion object {
        const val TOTAL_PREPARATION_TIME_MS = 2000L
    }
}
