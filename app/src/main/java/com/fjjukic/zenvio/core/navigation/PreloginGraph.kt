// In file: core/navigation/PreloginGraph.kt

package com.fjjukic.zenvio.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.fjjukic.zenvio.feature.prelogin.PreloginScreen
import com.fjjukic.zenvio.feature.walkthrough.ui.WalkthroughScreen

fun NavGraphBuilder.preloginGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Prelogin.route,
        route = Graph.PRELOGIN
    ) {
        composable(route = Screen.Prelogin.route) {
            PreloginScreen(
                onNavigateToNextScreen = {
                    navController.navigate(Screen.Walkthrough.route)
                }
            )
        }
        composable(route = Screen.Walkthrough.route) {
            WalkthroughScreen(
                onNavigateToNextScreen = {
                    navController.navigate(Graph.ONBOARDING) {
                        popUpTo(Graph.PRELOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
