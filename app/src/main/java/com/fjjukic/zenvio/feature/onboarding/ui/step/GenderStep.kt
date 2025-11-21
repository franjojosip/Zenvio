package com.fjjukic.zenvio.feature.onboarding.ui.step

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.onboarding.model.GenderType
import com.fjjukic.zenvio.feature.onboarding.model.GenderUi
import com.fjjukic.zenvio.feature.onboarding.model.mapGendersToUiModels
import com.fjjukic.zenvio.ui.defaults.AppInputDefaults
import com.fjjukic.zenvio.ui.theme.ZenvioTheme


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun GenderStepPreview() {
    ZenvioTheme {
        GenderStep(mapGendersToUiModels()) {}
    }
}

@Composable
fun GenderStep(genders: List<GenderUi>, onSelect: (GenderType) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            genders.filter { it.genderType != GenderType.OTHER }
                .forEach { gender ->
                    GenderButton(
                        item = gender,
                        onSelect = { onSelect(gender.genderType) }
                    )
                }
        }

        Spacer(Modifier.height(42.dp))

        genders.firstOrNull { it.genderType == GenderType.OTHER }?.let { gender ->
            OtherGenderButton(
                item = gender,
                onSelect = { onSelect(gender.genderType) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun GenderButtonSelectedPreview() {
    ZenvioTheme {
        GenderButton(
            item = GenderUi(
                genderType = GenderType.MALE,
                isSelected = true,
                textRes = R.string.label_gender_male,
                cdTextRes = R.string.cd_choice_gender_other,
                iconRes = R.drawable.ic_male,
            ),
            onSelect = {}
        )
    }
}

@Composable
private fun GenderButton(
    item: GenderUi,
    onSelect: () -> Unit
) {
    val textColor =
        if (item.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
            0.8f
        )
    val imageTint = if (item.isSelected) Color.White else Color.Black
    val boxBackgroundColor =
        if (item.isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.3f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onSelect() }
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(width = 1.dp, color = AppInputDefaults.borderColor, shape = CircleShape)
                .background(color = boxBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            item.iconRes?.let {
                Icon(
                    imageVector = ImageVector.vectorResource(it),
                    contentDescription = stringResource(item.textRes),
                    tint = imageTint,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
        Text(
            text = stringResource(item.textRes),
            modifier = Modifier.padding(top = 22.dp),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Normal,
                color = textColor
            ),
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun OtherGenderButtonSelectedPreview() {
    ZenvioTheme {
        OtherGenderButton(
            item = GenderUi(
                genderType = GenderType.OTHER,
                isSelected = true,
                textRes = R.string.label_gender_other,
                cdTextRes = R.string.cd_choice_gender_other,
            ),
            onSelect = {}
        )
    }
}


@Composable
private fun OtherGenderButton(
    item: GenderUi,
    onSelect: () -> Unit
) {
    val boxBackgroundColor =
        if (item.isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.3f)
    val textColor = if (item.isSelected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .height(60.dp)
            .clip(CircleShape)
            .border(width = 1.dp, color = AppInputDefaults.borderColor, shape = CircleShape)
            .background(color = boxBackgroundColor)
            .clickable { onSelect() }
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(item.textRes),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                textAlign = TextAlign.Center
            )
        )
    }
}