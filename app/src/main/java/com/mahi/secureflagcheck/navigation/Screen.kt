package com.mahi.secureflagcheck.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object ProductList : Screen("products")
    data object ProductDetail : Screen("product/{productId}") {
        fun create(productId: Int) = "product/$productId"
    }
    data object Cart : Screen("cart")
    data object Checkout : Screen("checkout")
    data object OrderDetails : Screen("order/{orderId}") {
        fun create(orderId: Int) = "order/$orderId"
    }
    data object OrderHistory : Screen("orders")
    data object MyAccount : Screen("account")
    data object Settings : Screen("settings")
}
