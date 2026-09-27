package com.fotis.bakeryorders.ui.Product_Item.Data.Products

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.fotis.bakeryorders.ui.Product_Item.Data.Bag.BagDao
import com.fotis.bakeryorders.ui.Product_Item.Data.Bag.BagData
import com.fotis.bakeryorders.ui.Product_Item.Data.Box.BoxDao
import com.fotis.bakeryorders.ui.Product_Item.Data.Box.BoxData
import com.fotis.bakeryorders.ui.Product_Item.Data.Cup.CupDao
import com.fotis.bakeryorders.ui.Product_Item.Data.Cup.CupData
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao, private val bagDao: BagDao, private val boxDao: BoxDao, private val cupDao: CupDao) {

    val allProducts: Flow<List<Product>> = productDao.getAllProduct()


    suspend fun  update(product: Product){
        productDao.update(product)
    }

    suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    suspend fun delete(product: Product) {
        productDao.delete(product)
    }

    suspend fun upsert(product: Product){
        productDao.upsert(product)
    }

    suspend fun deleteAll(){ // Add this function
        productDao.deleteAllProducts()
    }



    suspend fun increaseQuantity(productName: String) {
        productDao.increaseQuantity(productName)
    }

    suspend fun decreaseQuantity(productName: String) {
        productDao.decreaseQuantity(productName)
    }


   suspend fun countProducts(): Int {
        return productDao.countProducts()
   }
    suspend fun updateCupWithProduct(cup: CupData): LiveData<List<CupData>> {
        cupDao.updateCup(cup)  // First update the cup
        // Now get the updated list of cups and return it
        return cupDao.getAllCups().asLiveData()
    }

    suspend fun updateBagWithProduct(bag: BagData): LiveData<List<BagData>> {
        bagDao.updateBag(bag)  // First update the cup
        // Now get the updated list of cups and return it
        return bagDao.getAllBag().asLiveData()
    }

    suspend fun updateBoxWithProduct(box: BoxData): LiveData<List<BoxData>> {
        boxDao.updateBox(box)  // First update the cup
        // Now get the updated list of cups and return it
        return boxDao.getAllBox().asLiveData()
    }


    suspend fun insertCupWithProduct(cup: CupData) {
        cupDao.insertCup(cup)  // First update the cup
        // Now get the updated list of cups and return it
        //return cupDao.getAllCups().asLiveData()
    }

    suspend fun insertBagWithProduct(bag: BagData) {
        bagDao.insertBag(bag)  // First update the cup
        // Now get the updated list of cups and return it
        //return bagDao.getAllBag().asLiveData()
    }

    suspend fun insertBoxWithProduct(box: BoxData) {
        boxDao.insertBox(box)  // First update the cup
        // Now get the updated list of cups and return it
        //return boxDao.getAllBox().asLiveData()
    }
   suspend fun insertBoxWithProductAndReturn(box: BoxData):Long {
    return boxDao.insertBox(box)

    }

    suspend fun insertCupWithProductAndReturn(cup: CupData):Long {
        return cupDao.insertCup(cup)
    }

  suspend fun insertBagWithProductAndReturn(bag: BagData):Long {
        return bagDao.insertBag(bag)
    }



    suspend fun deleteCupWithProduct(cup: CupData) {
        cupDao.deleteCup(cup)

    }
    suspend fun deleteBagWithProduct(bag: BagData) {
        bagDao.deleteBag(bag)
    }
    suspend fun deleteBoxWithProduct(box: BoxData) {
        boxDao.deleteBox(box)
    }



    fun getCupsWithProduct(productName: String): LiveData<List<CupData>> =
        cupDao.getCupForProduct(productName).asLiveData()

    fun getBoxesWithProduct(productName: String): LiveData<List<BoxData>> =
        boxDao.getBoxForProduct(productName).asLiveData()

    fun getBagsWithProduct(productName: String): LiveData<List<BagData>> =
        bagDao.getBagForProduct(productName).asLiveData()



}