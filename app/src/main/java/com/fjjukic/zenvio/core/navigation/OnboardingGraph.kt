package com.fjjukic.zenvio.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.fjjukic.zenvio.feature.onboarding.ui.OnboardingScreen
import com.fjjukic.zenvio.feature.preparing_plan.ui.PreparingPlanScreen

fun NavGraphBuilder.onboardingGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Onboarding.route,
        route = Graph.ONBOARDING
    ) {
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToNextScreen = {
                    navController.navigate(Screen.PreparingPlan.route)
                }
            )
        }
        composable(route = Screen.PreparingPlan.route) {
            PreparingPlanScreen(
                onNavigateToNextScreen = {
                    navController.navigate(Graph.MAIN) {
                        popUpTo(Graph.ROOT) {
                            inclusive = false
                        }
                    }
                }
            )
        }
    }
}