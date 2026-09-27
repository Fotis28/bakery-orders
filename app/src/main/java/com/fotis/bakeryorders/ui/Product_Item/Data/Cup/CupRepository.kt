package com.fotis.bakeryorders.ui.Product_Item.Data.Cup

import kotlinx.coroutines.flow.Flow

class CupRepository(private val dao: CupDao) {
    fun getCupsForProduct(productName: String): Flow<List<CupData>> = dao.getCupForProduct(productName)
    suspend fun insert(cup: CupData) = dao.insertCup(cup)
    suspend fun update(cup: CupData) = dao.updateCup(cup)
    suspend fun delete(cup: CupData) = dao.deleteCup(cup)
}