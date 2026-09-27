package com.fotis.bakeryorders.ui.Product_Item.Data.Cup
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CupDao {

    @Query("SELECT * FROM cup WHERE productName = :productName")
    fun getCupForProduct(productName: String): Flow<List<CupData>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCup(cup: CupData):Long




    @Delete
    suspend fun deleteCup(cup: CupData)

    @Update
    suspend fun updateCup(cup: CupData)

    @Upsert
    fun upsertCup(cup: CupData)

    @Query("SELECT * FROM cup ")
    fun getAllCups(): Flow<List<CupData>>

    @Query("DELETE FROM cup WHERE productName = :productName")
    suspend fun deleteAllCupFromProduct(productName: String)
}