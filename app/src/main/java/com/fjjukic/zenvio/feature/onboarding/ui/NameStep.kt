package com.fjjukic.zenvio.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingStep
import com.fjjukic.zenvio.ui.defaults.AppInputDefaults
import com.fjjukic.zenvio.ui.theme.ZenvioTheme

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun NameStepPreview() {
    ZenvioTheme {
        NameStep(OnboardingStep.Name()) {}
    }
}

@Composable
fun NameStep(step: OnboardingStep.Name, onValueChange: (String) -> Unit) {
    BaseStep(
        titleRes = R.string.title_onboarding_name_step,
        subtitleRes = R.string.subtitle_onboarding_name_step
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(top = 32.dp)
                .border(
                    width = 1.5.dp,
                    color = AppInputDefaults.borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .background(color = AppInputDefaults.boxFillColor),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = step.name,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(0.8f),
                singleLine = true,
                textStyle = MaterialTheme.typography.displaySmall.copy(
                    color = AppInputDefaults.textFieldTextColor,
                    textAlign = TextAlign.Center
                ),
                decorationBox = { innerTextField ->
                    if (step.name.isEmpty()) {
                        Text(
                            text = stringResource(R.string.placeholder_onboarding_name),
                            style = MaterialTheme.typography.displaySmall.copy(
                                color = AppInputDefaults.hintColor,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    innerTextField()
                },
                cursorBrush = SolidColor(AppInputDefaults.cursorColor),
            )
        }
    }
}