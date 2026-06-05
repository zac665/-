package com.example.intelligentgodds.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.intelligentgodds.ui.screens.HomeScreen
import com.example.intelligentgodds.ui.screens.ProductDetailScreen
import com.example.intelligentgodds.viewmodel.ProductViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: ProductViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }
        
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("productId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                viewModel = viewModel,
                productId = productId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
