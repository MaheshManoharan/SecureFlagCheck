package com.mahi.secureflagcheck.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.mahi.secureflagcheck.data.FakeData
import com.mahi.secureflagcheck.data.Product
import com.mahi.secureflagcheck.data.inr
import com.mahi.secureflagcheck.ui.components.AppScaffold
import com.mahi.secureflagcheck.ui.components.LabelValue
import com.mahi.secureflagcheck.ui.components.ScreenColumn

@Composable
fun HomeScreen(
    onProducts: () -> Unit,
    onCart: () -> Unit,
    onOrders: () -> Unit,
    onAccount: () -> Unit,
    onSettings: () -> Unit,
) {
    AppScaffold(title = "Home") { padding ->
        ScreenColumn(padding) {
            Text("Welcome back!", style = MaterialTheme.typography.headlineSmall)
            Text("This screen is protected: screenshots and recordings show black.")
            Button(onClick = onProducts, modifier = Modifier.fillMaxWidth()) { Text("Browse products") }
            Button(onClick = onCart, modifier = Modifier.fillMaxWidth()) { Text("Cart") }
            Button(onClick = onOrders, modifier = Modifier.fillMaxWidth()) { Text("Order history") }
            OutlinedButton(onClick = onAccount, modifier = Modifier.fillMaxWidth()) { Text("My account") }
            OutlinedButton(onClick = onSettings, modifier = Modifier.fillMaxWidth()) { Text("Settings") }
        }
    }
}

@Composable
fun ProductListScreen(onBack: () -> Unit, onProduct: (Int) -> Unit) {
    AppScaffold(title = "Products", onBack = onBack) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(FakeData.products, key = { it.id }) { product ->
                ProductCard(product, onClick = { onProduct(product.id) })
            }
        }
    }
}

@Composable
private fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(16.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text(product.price.inr(), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ProductDetailScreen(productId: Int, onBack: () -> Unit, onGoToCart: () -> Unit) {
    val product = FakeData.product(productId)
    AppScaffold(title = product.name, onBack = onBack) { padding ->
        ScreenColumn(padding) {
            Text(product.name, style = MaterialTheme.typography.headlineSmall)
            Text(product.price.inr(), style = MaterialTheme.typography.titleLarge)
            Text(product.description)
            Button(onClick = onGoToCart, modifier = Modifier.fillMaxWidth()) { Text("Add to cart") }
        }
    }
}

@Composable
fun CartScreen(onBack: () -> Unit, onCheckout: () -> Unit) {
    val cart = FakeData.cart
    AppScaffold(title = "Cart", onBack = onBack) { padding ->
        ScreenColumn(padding) {
            cart.forEach { (product, qty) ->
                LabelValue("${product.name} × $qty", (product.price * qty).inr())
            }
            HorizontalDivider()
            LabelValue("Total", cart.sumOf { (p, q) -> p.price * q }.inr(), bold = true)
            Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth()) { Text("Checkout") }
        }
    }
}

@Composable
fun CheckoutScreen(onBack: () -> Unit, onPlaceOrder: () -> Unit) {
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var expiry by rememberSaveable { mutableStateOf("") }
    var cvv by rememberSaveable { mutableStateOf("") }
    val total = FakeData.cart.sumOf { (p, q) -> p.price * q }

    AppScaffold(title = "Checkout", onBack = onBack) { padding ->
        ScreenColumn(padding) {
            LabelValue("Amount payable", total.inr(), bold = true)
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it.filter(Char::isDigit).take(16) },
                label = { Text("Card number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = expiry,
                onValueChange = { expiry = it.take(5) },
                label = { Text("Expiry (MM/YY)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = cvv,
                onValueChange = { cvv = it.filter(Char::isDigit).take(4) },
                label = { Text("CVV") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Button(onClick = onPlaceOrder, modifier = Modifier.fillMaxWidth()) { Text("Pay ${total.inr()}") }
        }
    }
}
