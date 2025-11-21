package com.fjjukic.zenvio.feature.walkthrough.model

sealed interface WalkthroughIntent {
    object StartButtonClick : WalkthroughIntent
    object EndButtonClick : WalkthroughIntent
    data class PageChanged(val page: Int) : WalkthroughIntent

    object BackPressed : WalkthroughIntent
}