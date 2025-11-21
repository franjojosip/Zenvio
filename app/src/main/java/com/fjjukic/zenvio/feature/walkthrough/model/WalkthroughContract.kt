package com.fjjukic.zenvio.feature.walkthrough.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fjjukic.zenvio.R

sealed interface WalkthroughIntent {
    data class PageChanged(val page: Int) : WalkthroughIntent

    data object StartButtonClick : WalkthroughIntent
    data object EndButtonClick : WalkthroughIntent
    data object BackPressed : WalkthroughIntent
}

data class WalkthroughUiState(
    val pages: List<WalkthroughPage> = emptyList(),
    val currentPage: Int = 0
) {
    private val isFirstPage
        get() = currentPage == 0

    val isLastPage
        get() = currentPage == pages.lastIndex

    val startBtnText: Int?
        get() = if (!isFirstPage && !isLastPage) R.string.btn_back else null

    val endBtnText: Int
        get() = if (isLastPage) R.string.btn_lets_get_started else R.string.btn_continue
}

sealed interface WalkthroughEffect {
    data object WalkthroughCanceled : WalkthroughEffect
    data object WalkthroughFinished : WalkthroughEffect
}

data class WalkthroughPage(
    @field:DrawableRes val imageRes: Int,
    @field:StringRes val titleRes: Int,
    @field:StringRes val descriptionRes: Int
)