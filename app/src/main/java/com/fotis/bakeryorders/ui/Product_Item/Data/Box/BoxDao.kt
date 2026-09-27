package com.fotis.bakeryorders.ui.Product_Item.Data.Box


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert


import kotlinx.coroutines.flow.Flow

@Dao
interface BoxDao {

    @Query("SELECT * FROM box WHERE productName = :productName")
    fun getBoxForProduct(productName: String): Flow<List<BoxData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBox(box: BoxData):Long

    @Delete
    suspend fun deleteBox(box: BoxData)

    @Update
    suspend fun updateBox(box: BoxData)

    @Upsert
    fun upsertBox(box: BoxData)

    @Query("SELECT * FROM box ")
    fun getAllBox(): Flow<List<BoxData>>

    @Query("SELECT * FROM box ")
    fun getAllBoxs(): Flow<List<BoxData>>

    @Query("DELETE FROM box WHERE productName = :productName")
    suspend fun deleteAllBoxFromProduct(productName: String)

   /* // Update quantity based on productName (or other unique identifier)
    @Query("UPDATE box SET quantity = :newQuantity WHERE productName = :productName AND id = :BoxId")
    suspend fun updateBoxQuantity(newQuantity: Int, productName: String, boxId: Int)
*/
}