package com.fotis.bakeryorders.ui.Product_Item.Data.Products

import androidx.room.Embedded
import androidx.room.Relation
import com.fotis.bakeryorders.ui.Product_Item.Data.Bag.BagData
import com.fotis.bakeryorders.ui.Product_Item.Data.Box.BoxData
import com.fotis.bakeryorders.ui.Product_Item.Data.Cup.CupData


data class ProductWithBags(
    @Embedded val product: Product,

    @Relation(
        parentColumn = "productName",
        entityColumn = "productName",
        entity = BagData::class
    )
    val bag: List<BagData>,

    @Relation(
        parentColumn = "productName",
        entityColumn = "productName",
        entity = CupData::class
    )
    val cup: List<CupData>,
    @Relation(
        parentColumn = "productName",
        entityColumn = "productName",
        entity = BoxData::class
    )
    val box: List<BoxData>
)