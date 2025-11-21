package com.fjjukic.zenvio.feature.onboarding.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.core.util.CustomSystemBars
import com.fjjukic.zenvio.core.util.findActivity
import com.fjjukic.zenvio.feature.onboarding.OnboardingViewModel
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingIntent
import com.fjjukic.zenvio.feature.onboarding.model.OnboardingStep
import com.fjjukic.zenvio.ui.theme.DividerLight
import com.fjjukic.zenvio.ui.theme.ZenvioTheme

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onFinished: () -> Unit = {}
) {
    CustomSystemBars(lightStatusBarIcons = true, lightNavigationBarIcons = true)

    val activity = LocalContext.current.findActivity()

    BackHandler {
        viewModel.onIntent(OnboardingIntent.BackPressed)
    }

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            OnboardingTopBar(
                progress = state.progress,
                counterText = state.counterText,
                modifier = Modifier.statusBarsPadding(),
            ) {
                viewModel.onIntent(OnboardingIntent.BackPressed)
            }

        },
        bottomBar = {
            OnboardingBottomBar(
                modifier = Modifier.navigationBarsPadding(),
                btnTextRes = R.string.btn_continue,
                isBtnEnabled = state.isBtnEnabled
            ) {
                viewModel.onIntent(OnboardingIntent.Next)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val step = state.currentStep) {
                is OnboardingStep.Name -> {
                    NameStep(step) {
                        viewModel.onIntent(OnboardingIntent.UpdateName(it))
                    }
                }

                is OnboardingStep.Gender -> {
                    GenderStep(step.genders) {
                        viewModel.onIntent(OnboardingIntent.SelectGender(it))
                    }
                }

                is OnboardingStep.Age -> {
                    AgeStep(step.age) {
                        viewModel.onIntent(OnboardingIntent.SelectAge(it))
                    }
                }

                is OnboardingStep.ChoiceSelect -> {
                    SelectStep(step) {
                        viewModel.onIntent(OnboardingIntent.ToggleSelect(it))
                    }
                }

                null -> {
                    // do nothing
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                OnboardingViewModel.OnboardingEffect.OnboardingCanceled -> {
                    activity?.finish()
                }

                OnboardingViewModel.OnboardingEffect.OnboardingFinished -> {
                    onFinished()
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun OnboardingTopBarPreview() {
    ZenvioTheme {
        OnboardingTopBar(progress = 0.3f, "1 / 5", onBackClick = {})
    }
}

@Composable
fun OnboardingTopBar(
    progress: Float,
    counterText: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit) = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.clickable {
                onBackClick()
            }
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .height(12.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
            gapSize = 4.dp
        )
        Text(
            text = counterText,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun OnboardingBottomBarPreview() {
    ZenvioTheme {
        OnboardingBottomBar(
            btnTextRes = R.string.btn_continue,
            onBtnClick = {},
            isBtnEnabled = true
        )
    }
}

@Composable
fun OnboardingBottomBar(
    modifier: Modifier = Modifier,
    btnTextRes: Int,
    isBtnEnabled: Boolean,
    onBtnClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = DividerLight
        )
        Button(
            onClick = onBtnClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = isBtnEnabled
        ) { Text(stringResource(btnTextRes)) }
    }
}