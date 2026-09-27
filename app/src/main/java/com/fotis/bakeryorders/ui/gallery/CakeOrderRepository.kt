package com.fotis.bakeryorders.ui.gallery

import kotlinx.coroutines.flow.Flow

class CakeOrderRepository(private val cakeDataDao: CakeDataDao) {


    suspend fun insertCakeOrder(cakeOrder: CakeData){
        cakeDataDao.insertCake(cakeOrder)

    }

    suspend fun insertCakeOrderBack(cakeOrder: CakeData): Long {
        return cakeDataDao.insertCake(cakeOrder)
    }

    suspend fun updateCakeOrder(cakeOrder: CakeData) {
        cakeDataDao.upsertCake(cakeOrder)
    }

    suspend fun deleteCakeOrder(cakeOrder: CakeData) {
        cakeDataDao.deleteCake(cakeOrder)
    }

    fun getAllCakeOrders(): Flow<List<CakeData>> {
        return cakeDataDao.getAllCakeOrders()
    }

    suspend fun deleteAllCakeOrder(){
        cakeDataDao.deleteAllCakeOrder()
    }

    suspend fun getOrdersByDay(day: String): List<CakeData> {
        return cakeDataDao.getOrdersByDay(day)
    }

    suspend fun getOrdersByTime(time1: String, time2: String): List<CakeData> {
        return cakeDataDao.getOrdersBetweenTimes(time1, time2)
    }
   suspend fun getCheckedOrders(): List<CakeData> {
        return cakeDataDao.getCheckedOrders()

   }
    suspend fun getOrdersByType(type: String): List<CakeData> {
        return cakeDataDao.getOrdersByType(type)
    }

    suspend fun getOrdersByClientName(name: String): List<CakeData> {
        return cakeDataDao.getOrdersByClientName(name)
    }

    suspend fun getOrdersByPhone(phone: String): List<CakeData> {
        return cakeDataDao.getOrdersByPhone(phone)
    }






}