package com.mahi.secureflagcheck.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mahi.secureflagcheck.data.FakeData
import com.mahi.secureflagcheck.ui.screens.CartScreen
import com.mahi.secureflagcheck.ui.screens.CheckoutScreen
import com.mahi.secureflagcheck.ui.screens.HomeScreen
import com.mahi.secureflagcheck.ui.screens.MyAccountScreen
import com.mahi.secureflagcheck.ui.screens.OrderDetailsScreen
import com.mahi.secureflagcheck.ui.screens.OrderHistoryScreen
import com.mahi.secureflagcheck.ui.screens.ProductDetailScreen
import com.mahi.secureflagcheck.ui.screens.ProductListScreen
import com.mahi.secureflagcheck.ui.screens.SettingsScreen

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val back: () -> Unit = { nav.popBackStack() }

    NavHost(navController = nav, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onProducts = { nav.navigate(Screen.ProductList.route) },
                onCart = { nav.navigate(Screen.Cart.route) },
                onOrders = { nav.navigate(Screen.OrderHistory.route) },
                onAccount = { nav.navigate(Screen.MyAccount.route) },
                onSettings = { nav.navigate(Screen.Settings.route) },
            )
        }
        composable(Screen.ProductList.route) {
            ProductListScreen(onBack = back, onProduct = { nav.navigate(Screen.ProductDetail.create(it)) })
        }
        composable(
            Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType }),
        ) { entry ->
            ProductDetailScreen(
                productId = entry.arguments?.getInt("productId") ?: FakeData.products.first().id,
                onBack = back,
                onGoToCart = { nav.navigate(Screen.Cart.route) },
            )
        }
        composable(Screen.Cart.route) {
            CartScreen(onBack = back, onCheckout = { nav.navigate(Screen.Checkout.route) })
        }
        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onBack = back,
                onPlaceOrder = {
                    nav.navigate(Screen.OrderDetails.create(FakeData.orders.last().id)) {
                        popUpTo(Screen.Home.route)
                    }
                },
            )
        }
        composable(
            Screen.OrderDetails.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType }),
        ) { entry ->
            OrderDetailsScreen(orderId = entry.arguments?.getInt("orderId") ?: 0, onBack = back)
        }
        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(onBack = back, onOrder = { nav.navigate(Screen.OrderDetails.create(it)) })
        }
        composable(Screen.MyAccount.route) {
            MyAccountScreen(
                onBack = back,
                onOrders = { nav.navigate(Screen.OrderHistory.route) },
                onSettings = { nav.navigate(Screen.Settings.route) },
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = back)
        }
    }
}
