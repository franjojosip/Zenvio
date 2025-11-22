package com.fjjukic.zenvio.core.navigation

sealed class Screen(val route: String) {
    // Prelogin Flow
    data object Prelogin : Screen("prelogin_screen")
    data object Walkthrough : Screen("walkthrough_screen")

    // Onboarding Flow
    data object Onboarding : Screen("onboarding_screen")
    data object PreparingPlan : Screen("preparing_plan_screen")

    // Main App Flow
    data object Home : Screen("home_screen")
    data object Chat : Screen("chat_screen")
}