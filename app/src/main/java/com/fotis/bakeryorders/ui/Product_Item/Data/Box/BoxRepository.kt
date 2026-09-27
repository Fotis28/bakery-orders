package com.fotis.bakeryorders.ui.Product_Item.Data.Box

import kotlinx.coroutines.flow.Flow

class BoxRepository(private val dao: BoxDao) {
    fun getBoxesForProduct(productName: String): Flow<List<BoxData>> = dao.getBoxForProduct(productName)
    suspend fun insert(box: BoxData) = dao.insertBox(box)
    suspend fun update(box: BoxData) = dao.updateBox(box)
    suspend fun delete(box: BoxData) = dao.deleteBox(box)
}