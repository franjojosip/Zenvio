package com.fjjukic.zenvio.core.navigation

sealed class Screen(val route: String) {
    data object Prelogin : Screen("prelogin")
    data object Walkthrough : Screen("walkthrough")
    data object Onboarding : Screen("onboarding")
    data object PreparingPlan : Screen("preparing_plan")
    data object Home : Screen("home")
    data object Chat : Screen("chat")
}