package com.fjjukic.zenvio.feature.preparing_plan.model

sealed interface PreparingPlanIntent {
    data object Start : PreparingPlanIntent
}