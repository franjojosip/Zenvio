package com.fjjukic.zenvio.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.fjjukic.zenvio.viewmodel.MainActivityViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val viewModel: MainActivityViewModel = hiltViewModel()
    val startGraph by viewModel.startGraph.collectAsState()

    // This ensures NavHost is only composed once the start destination is known
    startGraph?.let { startDestination ->
        NavHost(
            navController = navController,
            route = Graph.ROOT, // Use the ROOT constant for the NavHost itself
            startDestination = startDestination
        ) {
            preloginGraph(navController)
            onboardingGraph(navController)
            mainGraph(navController)
        }
    }
}