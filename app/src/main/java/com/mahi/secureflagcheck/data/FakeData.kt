package com.mahi.secureflagcheck.data

data class Product(val id: Int, val name: String, val price: Double, val description: String)

data class Order(val id: Int, val date: String, val status: String, val items: List<Pair<Product, Int>>) {
    val total: Double get() = items.sumOf { (product, qty) -> product.price * qty }
}

object FakeData {
    val products = listOf(
        Product(1, "Wireless Headphones", 2999.0, "Over-ear, noise cancelling, 30h battery."),
        Product(2, "Smart Watch", 4499.0, "AMOLED display, heart-rate and SpO2 tracking."),
        Product(3, "Bluetooth Speaker", 1799.0, "Portable, waterproof, deep bass."),
        Product(4, "Power Bank", 1299.0, "20000 mAh, 22.5W fast charging."),
        Product(5, "USB-C Cable", 349.0, "1.5m braided, 60W charging."),
        Product(6, "Phone Case", 499.0, "Shockproof transparent case."),
    )

    val cart: List<Pair<Product, Int>> = listOf(products[0] to 1, products[3] to 2)

    val orders = listOf(
        Order(1001, "02 Oct 2026", "Delivered", listOf(products[1] to 1)),
        Order(1002, "08 Oct 2026", "Shipped", listOf(products[2] to 1, products[4] to 2)),
        Order(1003, "13 Oct 2026", "Processing", listOf(products[5] to 3)),
    )

    fun product(id: Int) = products.first { it.id == id }
    fun order(id: Int) = orders.firstOrNull { it.id == id } ?: orders.first()
}

fun Double.inr(): String = "₹%,.2f".format(this)
