    package com.fotis.bakeryorders.ui.Product_Item.Data.Products

    import androidx.room.Entity
    import androidx.room.Ignore
    import androidx.room.PrimaryKey


    @Entity(tableName = "products")
    data class Product(
        @PrimaryKey val productName: String, // "Box", "Bug", "Cup"
        var quantity: Int,

        @Ignore
        var isExpanded: Boolean = false
    ) {
        constructor(productName: String, quantity: Int) : this(productName, quantity, false)
    }
