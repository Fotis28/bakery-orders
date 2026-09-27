package com.fotis.bakeryorders.ui.gallery

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CakeDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCake(cakeData: CakeData):Long

    @Delete
    suspend fun deleteCake(cakeData: CakeData)

    @Upsert
    suspend fun upsertCake(cakeData: CakeData)

    @Query("SELECT * FROM cakeOrder")
    fun getAllCakeOrders(): Flow<List<CakeData>>

    @Query("DELETE FROM cakeOrder"  )
     suspend fun deleteAllCakeOrder()

    @Query("SELECT * FROM cakeOrder WHERE Day = :day")
    suspend fun getOrdersByDay(day: String): List<CakeData>

    @Query("SELECT * FROM cakeOrder WHERE IsChecked = 1")
    suspend fun getCheckedOrders(): List<CakeData>

    @Query("SELECT * FROM cakeOrder WHERE Eidos = :type")
    suspend fun getOrdersByType(type: String): List<CakeData>

    @Query("SELECT * FROM cakeOrder WHERE NameAndLastName LIKE '%' || :name || '%'")
    suspend fun getOrdersByClientName(name: String): List<CakeData>

    @Query("SELECT * FROM cakeOrder WHERE Time BETWEEN :start AND :end")
    suspend fun getOrdersBetweenTimes(start: String, end: String): List<CakeData>

  @Query("SELECT * FROM cakeOrder WHERE Tilefono = :phone")
  suspend fun getOrdersByPhone(phone: String): List<CakeData>


}