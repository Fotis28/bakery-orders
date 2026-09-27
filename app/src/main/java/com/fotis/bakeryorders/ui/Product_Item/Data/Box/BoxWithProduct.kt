package com.fotis.bakeryorders.ui.Product_Item.Data.Box

import androidx.room.Embedded
import androidx.room.Relation
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.Product

data class BoxWithProduct(
    @Embedded val box: BoxData,

    @Relation(
        parentColumn = "productName",
        entityColumn = "productName"
    )
    val product: Product
)
