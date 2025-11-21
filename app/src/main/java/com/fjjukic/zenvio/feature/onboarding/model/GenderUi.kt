package com.fjjukic.zenvio.feature.onboarding.model

data class GenderUi(
    val genderType: GenderType,
    val isSelected: Boolean,
    val textRes: Int,
    val iconRes: Int? = null
)