package com.fjjukic.zenvio.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fjjukic.zenvio.feature.chat.ChatScreen
import com.fjjukic.zenvio.feature.home.HomeRoute
import com.fjjukic.zenvio.feature.onboarding.ui.OnboardingScreen
import com.fjjukic.zenvio.feature.prelogin.PreloginScreen
import com.fjjukic.zenvio.feature.preparing_plan.ui.PreparingPlanScreen
import com.fjjukic.zenvio.feature.walkthrough.ui.WalkthroughScreen
import com.fjjukic.zenvio.viewmodel.MainActivityViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    val viewModel: MainActivityViewModel = hiltViewModel()
    val startDestination = viewModel.startDestination.collectAsState().value ?: return


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Prelogin.route) {
            PreloginScreen {
                navController.navigate(Screen.Walkthrough.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }

        composable(Screen.Walkthrough.route) {
            WalkthroughScreen {
                navController.navigate(Screen.Onboarding.route)
            }
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen {
                navController.navigate(Screen.PreparingPlan.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }

        composable(Screen.PreparingPlan.route) {
            PreparingPlanScreen {
                navController.navigate(Screen.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }

        composable(Screen.Home.route) {
            HomeRoute(
                showChatScreen = {
                    navController.navigate(Screen.Chat.route)
                }
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen()
        }
    }
}
