// In file: core/navigation/MainGraph.kt

package com.fjjukic.zenvio.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.fjjukic.zenvio.feature.chat.ui.ChatScreen
import com.fjjukic.zenvio.feature.home.ui.HomeRoute

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Home.route,
        route = Graph.MAIN
    ) {
        composable(route = Screen.Home.route) {
            HomeRoute(
                showChatScreen = {
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
        composable(route = Screen.Chat.route) {
            ChatScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
