package com.fjjukic.zenvio.feature.walkthrough

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.core.data.preferences.DataStorePrefsManager
import com.fjjukic.zenvio.feature.walkthrough.data.WalkthroughRepository
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughEffect
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughIntent
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalkthroughViewModel @Inject constructor(
    private val sharedPrefsManager: DataStorePrefsManager,
    private val repository: WalkthroughRepository
) : ViewModel() {
    private val _effect = MutableSharedFlow<WalkthroughEffect>()
    val effect = _effect.asSharedFlow()

    private val _uiState = MutableStateFlow(WalkthroughUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(pages = repository.getPages()) }
    }

    fun onIntent(intent: WalkthroughIntent) {
        when (intent) {
            WalkthroughIntent.StartButtonClick -> previousPage()
            WalkthroughIntent.EndButtonClick -> handleNextButtonClick()
            is WalkthroughIntent.PageChanged -> updatePage(intent.page)
            WalkthroughIntent.BackPressed -> handleBackPressed()
        }
    }

    private fun handleNextButtonClick() {
        if (_uiState.value.isLastPage) {
            finishWalkthrough()
        } else {
            nextPage()
        }
    }

    private fun previousPage() {
        _uiState.update {
            it.copy(
                currentPage = it.currentPage - 1
            )
        }
    }

    private fun nextPage() {
        _uiState.update {
            if (it.isLastPage) {
                it
            } else {
                it.copy(currentPage = it.currentPage + 1)
            }
        }
    }


    private fun updatePage(page: Int) {
        _uiState.update { it.copy(currentPage = page) }
    }

    private fun handleBackPressed() {
        previousPage()
    }

    private fun finishWalkthrough() {
        viewModelScope.launch {
            sharedPrefsManager.setWalkthroughCompleted(true)
            _effect.emit(WalkthroughEffect.WalkthroughFinished)
        }
    }
}