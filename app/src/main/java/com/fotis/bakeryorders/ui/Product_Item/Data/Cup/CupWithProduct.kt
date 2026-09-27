package com.fotis.bakeryorders.ui.Product_Item.Data.Cup

import androidx.room.Embedded
import androidx.room.Relation
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.Product

 data class CupWithProduct (@Embedded val cup: CupData,

                            @Relation(
                          parentColumn = "productName",
                          entityColumn = "productName"
                      )
                      val product: Product
)
