package com.fotis.bakeryorders.ui.Product_Item.Data.Bag

import kotlinx.coroutines.flow.Flow

class BagRepository(private val dao: BagDao) {
    fun getBagsForProduct(productName: String): Flow<List<BagData>> = dao.getBagForProduct(productName)
    suspend fun insert(bag: BagData) = dao.insertBag(bag)
    suspend fun update(bag: BagData) = dao.updateBag(bag)
    suspend fun delete(bag: BagData) = dao.deleteBag(bag)
}