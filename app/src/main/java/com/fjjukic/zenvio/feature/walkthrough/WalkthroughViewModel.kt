package com.fjjukic.zenvio.feature.walkthrough

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.core.data.preferences.PrefsManager
import com.fjjukic.zenvio.core.data.repository.WalkthroughRepository
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughIntent
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class WalkthroughViewModel @Inject constructor(
    private val sharedPrefsManager: PrefsManager,
    private val repository: WalkthroughRepository
) : ViewModel() {
    private val _effect = MutableSharedFlow<WalkthroughEffect>()
    val effect: SharedFlow<WalkthroughEffect> = _effect

    private val _uiState = MutableStateFlow<WalkthroughUiState>(WalkthroughUiState())
    val uiState: StateFlow<WalkthroughUiState> = _uiState


    init {
        loadData()
    }

    private fun loadData() {
        _uiState.value = _uiState.value.copy(pages = repository.getPages())
    }

    fun onIntent(intent: WalkthroughIntent) {
        when (intent) {
            WalkthroughIntent.StartButtonClick -> previousPage()
            WalkthroughIntent.EndButtonClick -> handleNextButtonClick()
            is WalkthroughIntent.PageChanged -> updatePage(intent.page)
            WalkthroughIntent.BackPressed -> handleBackPressed()
        }
    }

    private fun handleBackPressed() {
        val state = _uiState.value

        if (!state.isFirstPage) {
            previousPage()
        } else {
            viewModelScope.launch {
                sendEffect(WalkthroughEffect.WalkthroughCanceled)
            }
        }
    }

    private suspend fun sendEffect(effect: WalkthroughEffect) {
        _effect.emit(effect)
    }

    private fun previousPage() {
        _uiState.value = _uiState.value.copy(
            currentPage = _uiState.value.currentPage - 1
        )
    }


    private fun handleNextButtonClick() {
        if (_uiState.value.isLastPage) {
            finishWalkthrough()
        } else {
            nextPage()
        }
    }

    private fun finishWalkthrough() {
        viewModelScope.launch {
            sharedPrefsManager.setWalkthroughCompleted(true)
            sendEffect(WalkthroughEffect.WalkthroughFinished)
        }
    }


    private fun nextPage() {
        _uiState.value = _uiState.value.copy(
            currentPage = _uiState.value.currentPage + 1
        )
    }

    private fun updatePage(page: Int) {
        _uiState.value = _uiState.value.copy(currentPage = page)
    }

    data class WalkthroughUiState(
        val pages: List<WalkthroughPage> = emptyList(),
        val currentPage: Int = 0
    ) {
        val isFirstPage get() = currentPage == 0

        val isLastPage get() = currentPage == pages.lastIndex

        val startBtnText: Int?
            get() = if (!isFirstPage && !isLastPage) R.string.btn_back else null

        val endBtnText: Int
            get() = if (isLastPage) R.string.btn_lets_get_started else R.string.btn_continue
    }

    sealed class WalkthroughEffect {
        data object WalkthroughCanceled : WalkthroughEffect()
        data object WalkthroughFinished : WalkthroughEffect()
    }
}