package com.mahi.secureflagcheck.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mahi.secureflagcheck.data.FakeData
import com.mahi.secureflagcheck.data.inr
import com.mahi.secureflagcheck.ui.components.AppScaffold
import com.mahi.secureflagcheck.ui.components.LabelValue
import com.mahi.secureflagcheck.ui.components.ScreenColumn

@Composable
fun OrderDetailsScreen(orderId: Int, onBack: () -> Unit) {
    val order = FakeData.order(orderId)
    AppScaffold(title = "Order #${order.id}", onBack = onBack) { padding ->
        ScreenColumn(padding) {
            LabelValue("Date", order.date)
            LabelValue("Status", order.status)
            HorizontalDivider()
            order.items.forEach { (product, qty) ->
                LabelValue("${product.name} × $qty", (product.price * qty).inr())
            }
            HorizontalDivider()
            LabelValue("Total", order.total.inr(), bold = true)
        }
    }
}

@Composable
fun OrderHistoryScreen(onBack: () -> Unit, onOrder: (Int) -> Unit) {
    AppScaffold(title = "Order history", onBack = onBack) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(FakeData.orders, key = { it.id }) { order ->
                Card(modifier = Modifier.fillMaxWidth().clickable { onOrder(order.id) }) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Order #${order.id}", style = MaterialTheme.typography.titleMedium)
                        Text("${order.date} · ${order.status}")
                        Text(order.total.inr())
                    }
                }
            }
        }
    }
}

@Composable
fun MyAccountScreen(onBack: () -> Unit, onOrders: () -> Unit, onSettings: () -> Unit) {
    AppScaffold(title = "My account", onBack = onBack) { padding ->
        ScreenColumn(padding) {
            LabelValue("Name", "Mahi Sharma")
            LabelValue("Email", "mahi@example.com")
            LabelValue("Phone", "+91 98765 43210")
            LabelValue("Address", "221B, MG Road, Bengaluru")
            Button(onClick = onOrders, modifier = Modifier.fillMaxWidth()) { Text("My orders") }
            Button(onClick = onSettings, modifier = Modifier.fillMaxWidth()) { Text("Settings") }
        }
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var notifications by rememberSaveable { mutableStateOf(true) }
    var biometric by rememberSaveable { mutableStateOf(false) }
    AppScaffold(title = "Settings", onBack = onBack) { padding ->
        ScreenColumn(padding) {
            SettingRow("Notifications", notifications) { notifications = it }
            SettingRow("Biometric login", biometric) { biometric = it }
        }
    }
}

@Composable
private fun SettingRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
