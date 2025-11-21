package com.fjjukic.zenvio.feature.onboarding.model

import com.fjjukic.zenvio.R

enum class GenderType() {
    MALE,
    FEMALE,
    OTHER
}

fun getGenders(): List<GenderUi> {
    return GenderType.entries.toTypedArray().mapIndexed { index, type ->
        when (type) {
            GenderType.MALE -> GenderUi(
                genderType = type,
                isSelected = index == 0,
                textRes = R.string.label_gender_male,
                iconRes = R.drawable.ic_male,
            )

            GenderType.FEMALE -> GenderUi(
                genderType = type,
                isSelected = index == 0,
                textRes = R.string.label_gender_female,
                iconRes = R.drawable.ic_female,
            )

            GenderType.OTHER -> GenderUi(
                genderType = type,
                isSelected = index == 0,
                textRes = R.string.label_gender_other,
            )
        }
    }
}