package com.fjjukic.zenvio.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.fjjukic.zenvio.feature.breathing.ui.BreathingScreen
import com.fjjukic.zenvio.feature.chat.ui.ChatScreen
import com.fjjukic.zenvio.feature.home.model.HomeTab
import com.fjjukic.zenvio.feature.home.ui.HomeRoute
import com.fjjukic.zenvio.feature.sleep.ui.SleepScreen

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        route = Graph.MAIN,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeRoute(
                showChatScreen = {
                    navController.navigate(Screen.Chat.route)
                },
                onTabClick = {
                    handleTab(HomeTab.HOME, it, navController)
                }
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Sleep.route) {
            SleepScreen(
                onBreathingClick = {
                    navController.navigate(Screen.Breathing.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTabClick = {
                    handleTab(HomeTab.SLEEP, it, navController)
                }
            )
        }

        composable(Screen.Breathing.route) {
            BreathingScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

fun handleTab(
    currentTab: HomeTab,
    clickedTab: HomeTab,
    navController: NavHostController
) {
    if (currentTab == clickedTab) return
    navController.navigateFromHome(clickedTab.route())
}

fun NavHostController.navigateFromHome(route: String) {
    if (route == Screen.Home.route) {
        popBackStack(Screen.Home.route, false)
    } else {
        navigate(route) {
            popUpTo(Screen.Home.route) { inclusive = false }
            launchSingleTop = true
        }
    }
}

fun HomeTab.route(): String = when (this) {
    HomeTab.HOME -> Screen.Home.route
//    HomeTab.SLEEP -> Screen.Sleep.route
    //HomeTab.EXPLORE -> Screen.Explore.route
    //HomeTab.INSIGHTS -> Screen.Insights.route
    //HomeTab.ACCOUNT -> Screen.Account.route
    else -> Screen.Home.route
}