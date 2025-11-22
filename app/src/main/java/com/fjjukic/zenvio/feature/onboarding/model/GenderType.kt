package com.fjjukic.zenvio.feature.onboarding.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fjjukic.zenvio.R

enum class GenderType(
    @StringRes val textRes: Int,
    @StringRes val cdTextRes: Int,
    @DrawableRes val iconRes: Int?
) {
    MALE(R.string.label_gender_male, R.string.cd_choice_gender_male, R.drawable.ic_male),
    FEMALE(R.string.label_gender_female, R.string.cd_choice_gender_female, R.drawable.ic_female),
    OTHER(R.string.label_gender_other, R.string.cd_choice_gender_other, null)
}

fun mapGendersToUiModels(selectedGender: GenderType? = null): List<GenderUi> {
    return GenderType.entries.map { genderType ->
        GenderUi(
            genderType = genderType,
            isSelected = genderType == selectedGender,
            textRes = genderType.textRes,
            cdTextRes = genderType.cdTextRes,
            iconRes = genderType.iconRes
        )
    }
}

data class GenderUi(
    val genderType: GenderType,
    val isSelected: Boolean,
    @StringRes val textRes: Int,
    @StringRes val cdTextRes: Int,
    @DrawableRes val iconRes: Int? = null
)