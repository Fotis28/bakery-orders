package com.fotis.bakeryorders.ui.Product_Item.Data.Bag

import androidx.room.Embedded
import androidx.room.Relation
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.Product

data class BagWithProduct(
    @Embedded val bag: BagData,

    @Relation(
        parentColumn = "productName",
        entityColumn = "productName"
    )
    val product: Product
)
