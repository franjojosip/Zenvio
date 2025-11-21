package com.fjjukic.zenvio.feature.onboarding.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.ui.theme.ZenvioTheme


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun BaseStepPreview() {
    ZenvioTheme {
        BaseStep(
            R.string.title_onboarding_name_step,
            R.string.subtitle_onboarding_name_step,
        ) { }
    }
}


@Composable
fun BaseStep(
    titleRes: Int,
    subtitleRes: Int,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(titleRes),
            modifier = Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
        )
        Text(
            stringResource(subtitleRes),
            modifier = Modifier.padding(top = 14.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                lineHeight = 24.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
            )
        )

        content()
    }
}