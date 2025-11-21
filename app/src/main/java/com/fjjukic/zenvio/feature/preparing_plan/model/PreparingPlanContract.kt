package com.fjjukic.zenvio.feature.preparing_plan.model

sealed interface PreparingPlanIntent {
    data object Start : PreparingPlanIntent
}

sealed interface PreparingPlanEffect {
    data object OnFinished : PreparingPlanEffect
}