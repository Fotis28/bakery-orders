package com.fotis.bakeryorders.ui.Product_Item.Data.Cup

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.Product

@Entity(
    tableName = "cup",
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = ["productName"],       // refers to Product.name
        childColumns = ["productName"], // must match Cup_data field name
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class CupData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "productName")
    val productName: String, // Foreign key to Product.name

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "quantity")
    var quantity: Int,

    var isExpanded: Boolean = false
){ fun toMapCup(): Map<String, Any> {
    return mapOf(
        "id" to id,
        "productName" to productName,
        "name" to name,
        "quantity" to quantity,
        "isExpanded" to isExpanded
    )
}
}
