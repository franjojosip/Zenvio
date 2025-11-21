package com.fjjukic.zenvio.feature.onboarding.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingStep
import com.fjjukic.zenvio.feature.onboarding.model.SelectChoiceUi
import com.fjjukic.zenvio.ui.defaults.AppInputDefaults
import com.fjjukic.zenvio.ui.theme.ZenvioTheme


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MultiSelectStepPreview() {
    ZenvioTheme {
        SelectStep(
            OnboardingStep.ChoiceSelect(
                titleRes = R.string.title_onboarding_main_goals_step,
                subtitleRes = R.string.subtitle_onboarding_main_goals_step,
                choices = listOf(
                    SelectChoiceUi(
                        1,
                        R.string.choice_work_school,
                        false
                    ),
                    SelectChoiceUi(
                        2,
                        R.string.choice_relationships,
                        true
                    )
                )
            )
        ) {}
    }
}

@Composable
fun SelectStep(step: OnboardingStep.ChoiceSelect, onClick: (Int) -> Unit) {
    val listState = rememberLazyListState()
    BaseStep(
        titleRes = step.titleRes,
        subtitleRes = step.subtitleRes,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
        ) {
            items(step.choices, key = { it.id }) { choice ->
                ChoiceCard(choice, onClick)
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MultiSelectChoiceSelectedPreview() {
    ZenvioTheme {
        ChoiceCard(
            SelectChoiceUi(
                1,
                R.string.choice_work_school,
                false
            )
        ) {}
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SelectChoiceNotSelectedPreview() {
    ZenvioTheme {
        ChoiceCard(
            SelectChoiceUi(
                1,
                R.string.choice_work_school,
                true
            )
        ) {}
    }
}

@Composable
fun ChoiceCard(
    choice: SelectChoiceUi,
    onClick: (Int) -> Unit
) {
    val borderColor =
        if (choice.isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Black.copy(alpha = 0.1f)
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                border = BorderStroke(1.5.dp, borderColor),
                RoundedCornerShape(8.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick(choice.id) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(choice.textRes),
            color = AppInputDefaults.textFieldTextColor,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.weight(1f)
        )

        if (choice.isSelected) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(28.dp)
            )
        }
    }
}
