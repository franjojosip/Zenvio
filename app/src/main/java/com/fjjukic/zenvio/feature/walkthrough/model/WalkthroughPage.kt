package com.fjjukic.zenvio.feature.walkthrough.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class WalkthroughPage(
    @field:DrawableRes val imageRes: Int,
    @field:StringRes val titleRes: Int,
    @field:StringRes val descriptionRes: Int
)