package com.fotis.bakeryorders.ui.Product_Item.Data.Products

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.fotis.bakeryorders.ui.Product_Item.Data.Bag.BagWithProduct
import com.fotis.bakeryorders.ui.Product_Item.Data.Box.BoxWithProduct
import com.fotis.bakeryorders.ui.Product_Item.Data.Cup.CupWithProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProduct(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Delete
    suspend fun delete(product: Product)


    @Update
    suspend fun update(product: Product)


    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    @Upsert
    suspend fun upsert(product: Product)


    // Πλήθος προϊόντων (για έλεγχο αρχικοποίησης)
    @Query("SELECT COUNT(*) FROM products")
    suspend fun countProducts(): Int

    @Query("SELECT * FROM products ORDER BY productName ASC")
    fun getProductOrderByName(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY quantity ASC")
    fun getProductOrderByQuantity(): Flow<List<Product>>

    // Συνδυασμός προϊόντων με τα Bags μέσω JOIN
    @Transaction // Ασφαλια για τα join αν δεν κανουν σωστα join   τοτε δεν γινεται ο υπολογισμος για τα join
    @Query("SELECT * FROM products")
    fun getAllProductsWithDetails(): Flow<List<ProductWithBags>>

    @Transaction // Ασφαλια για τα join αν δεν κανουν σωστα join   τοτε δεν γινεται ο υπολογισμος για τα join
    @Query("SELECT * FROM products INNER JOIN cup ON products.productName = cup.productName WHERE products.productName IN ('CUP')" )
    fun getCupsWithProduct(): LiveData<List<CupWithProduct>>

    @Transaction // Ασφαλια για τα join αν δεν κανουν σωστα join   τοτε δεν γινεται ο υπολογισμος για τα join
    @Query("SELECT * FROM products INNER JOIN box ON products.productName = box.productName WHERE products.productName IN ('BOX')" )
    fun getBoxesWithProduct(): LiveData<List<BoxWithProduct>>

    @Transaction // Ασφαλια για τα join αν δεν κανουν σωστα join   τοτε δεν γινεται ο υπολογισμος για τα join
    @Query("SELECT * FROM products INNER JOIN bag ON products.productName = bag.productName WHERE products.productName IN ('BAG')" )
    fun getBagsWithProduct(): LiveData<List<BagWithProduct>>

    @Query("UPDATE products SET quantity = quantity + 1 WHERE productName = :name")
    suspend fun increaseQuantity(name: String)

    @Query("UPDATE products SET quantity = quantity - 1 WHERE productName = :name AND quantity > 0")
    suspend fun decreaseQuantity(name: String)



}



