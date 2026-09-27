package com.fotis.bakeryorders.ui.Product_Item.Data.Products

// Base class for products
sealed class ProductType {
    abstract val product: Product

    data class Cup(val cupData: Cup_data, override val product: Product) : ProductType()
    data class Box(val boxData: Box_data, override val product: Product) : ProductType()
    data class Bag(val bagData: Bag_data, override val product: Product) : ProductType()
}

// Cup_data
data class Cup_data(val name: String, val quantity: Int)

// Box_data
data class Box_data(val name: String, val quantity: Int)

// Bag_data
data class Bag_data(val name: String, val quantity: Int)
