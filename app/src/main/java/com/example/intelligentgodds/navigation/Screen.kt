package com.example.intelligentgodds.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
}
