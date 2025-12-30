package com.fjjukic.zenvio.core.navigation

sealed class Screen(val route: String) {

    // Prelogin
    data object Prelogin : Screen("prelogin")
    data object Walkthrough : Screen("prelogin/walkthrough")

    // Onboarding
    data object Onboarding : Screen("onboarding")
    data object PreparingPlan : Screen("onboarding/preparing_plan")

    // Home tab
    data object Home : Screen("home")
    data object Chat : Screen("home/chat")

    // Sleep tab
    data object Sleep : Screen("sleep")
    data object Breathing : Screen("sleep/breathing")
}
