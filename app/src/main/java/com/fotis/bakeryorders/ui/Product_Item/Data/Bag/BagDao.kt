package com.fotis.bakeryorders.ui.Product_Item.Data.Bag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


import kotlinx.coroutines.flow.Flow



@Dao
interface BagDao {

    @Query("SELECT * FROM bag WHERE productName = :productName")
    fun getBagForProduct(productName: String): Flow<List<BagData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBag(bag: BagData):Long

    @Delete
    suspend fun deleteBag(bag: BagData)

    @Update
    suspend fun updateBag(bag: BagData)

    @Query("SELECT * FROM bag ")
    fun getAllBag(): Flow<List<BagData>>

    @Query("DELETE FROM bag WHERE productName = :productName")
    suspend fun deleteAllBagFromProduct(productName: String)
}


